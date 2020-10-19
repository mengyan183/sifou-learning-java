/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassCSourceCodeDemo
 * 对于 Class.java对应的jvm源码分析
 *
 * @author guoxing
 * @date 2020/10/19 3:46 PM
 * @since
 */
@Slf4j
public class ClassCSourceCodeDemo {
    /**
     * 对于 java.lang.Class.java
     * 在 openjdk中存在 jdk15-0dabbdfd97e6/src/java.base/share/native/libjava/Class.c
     * 在其中存在 registerNatives方法 会将所有定义的methods 进行注册,对于methods中存在了相关 java native method 和 c++ 对应的 method
     *
     * 在其中有一个特殊的方法为 getSuperclass ,其在methods中没有其对应的c++ method而是调用的jni.h中定义的GetSuperclass
     * 在jni.cpp中存在以下代码 为当前方法的具体实现
     * JNI_ENTRY(jclass, jni_GetSuperclass(JNIEnv *env, jclass sub))
     *   JNIWrapper("GetSuperclass");
     *
     *   HOTSPOT_JNI_GETSUPERCLASS_ENTRY(env, sub);
     *
     *   jclass obj = NULL;
     *   DT_RETURN_MARK(GetSuperclass, jclass, (const jclass&)obj);
     *
     *   // 对于 JNIHandles::resolve_non_null 是将java对象转换为 c++ 的 oop
     *   // 对于 JNIHandles::resolve_non_null 是 src/hotspot/share/runtime/jniHandles.hpp 中定义的静态方法
     *   // 在对应的 src/hotspot/share/runtime/jniHandles.inline.hpp 中存在其具体的实现方法 inline oop JNIHandles::resolve_non_null(jobject handle)
     *   // 其实际是调用的 inline oop JNIHandles::resolve_impl(jobject handle)  最终是返回的 AccessInternal::OopLoadProxy 来生成一个对象
     *   oop mirror = JNIHandles::resolve_non_null(sub);
     *   // primitive classes return NULL
     *   // 对于原生类型的判断,如果为原生类型则直接返回空原因为原生类型不可能存在super
     *   if (java_lang_Class::is_primitive(mirror)) return NULL; // int.class.getSuperclass();
     *
     *   // Rules of Class.getSuperClass as implemented by KLass::java_super:
     *   // arrays return Object
     *   // interfaces return NULL
     *   // proper classes return Klass::super()
     *   // 如果是一个非原生类型的数据,会将其转换为 Klass
     *   // 对于klass的集成关系可以看到 Klass -> Metadata -> MetaspaceObj
     *   // 根据其继承关系可以看出为什么说 java的class 存在于 Meta空间中;
     *   Klass* k = java_lang_Class::as_Klass(mirror);
     *   if (k->is_interface()) return NULL;
     *
     *   // return mirror for superclass
     *   Klass* super = k->java_super();
     *   // super2 is the value computed by the compiler's getSuperClass intrinsic:
     *   debug_only(Klass* super2 = ( k->is_array_klass()
     *                                  ? SystemDictionary::Object_klass()
     *                                  : k->super() ) );
     *   assert(super == super2,
     *          "java_super computation depends on interface, array, other super");
     *   obj = (super == NULL) ? NULL : (jclass) JNIHandles::make_local(super->java_mirror());
     *   return obj;
     * JNI_END
     */

    /**
     * 在 src/hotspot/share/memory/allocation.hpp 中定义了 以下几类内存空间
     *  ResourceObj : For objects allocated in the resource area (see resourceArea.hpp).
     *  CHeapObj : For objects allocated in the C-heap (managed by: free & malloc and tracked with NMT)
     *  StackObj : For objects allocated on the stack.
     *  AllStatic : For classes used as name spaces.
     *  MetaspaceObj : For classes in Metaspace (class data)
     *
     *  在 Metaspace中主要包含以下数据
     *   Class 编译后的 class数据
     *   Symbol 对应的java class 修饰符 Modifier.java
     *   f(TypeArrayU1) \
     *   f(TypeArrayU2) \
     *   f(TypeArrayU4) \
     *   f(TypeArrayU8) \
     *   f(TypeArrayOther) \ // 定义的相关数组数据
     *   f(Method) \ // 方法签名 在 Method 中包含以下类型数据 ConstMethod / MethodData / MethodCounters 等相关数据
     *   f(ConstMethod) \ // 由于java中的方法当编译为class之后都是不可变的,对于这些数据都是不可变数据,因此数据常量数据
     *   f(MethodData) \ // 对于methodData中包含了以下数据 header klass 当前方法所属class/ 当前方法的大小 / 数据变量
     *   f(ConstantPool) \ // 常量数据 对于 constanPool 实际是由  ConstantPool + ConstantPoolCache 组成
     *   f(ConstantPoolCache) \ // 常量池缓存
     *   f(Annotations) \ // 注解
     *   f(MethodCounters) \ //
     *   f(RecordComponent)
     * @param args
     */
    /**
     * java.lang.String#intern() 分析
     * 对于其native方法对应的是 src/java.base/share/native/libjava/String.c 中的 Java_java_lang_String_intern
     * -> jvm.h中的 JVM_InternString 其实现为 src/hotspot/share/prims/jvm.cpp 中的 JVM_ENTRY(jstring, JVM_InternString(JNIEnv *env, jstring str))
     * JVM_ENTRY(jstring, JVM_InternString(JNIEnv *env, jstring str))
     *   JVMWrapper("JVM_InternString");
     *   JvmtiVMObjectAllocEventCollector oam;
     *   if (str == NULL) return NULL;
     *   oop string = JNIHandles::resolve_non_null(str); // 转换为 oop 对象
     *   oop result = StringTable::intern(string, CHECK_NULL); // 在这里实际调用的是stringTable.cpp 中的 oop StringTable::intern(oop string, TRAPS)
     *   return (jstring) JNIHandles::make_local(env, result);
     * JVM_END
     *
     * // StringTable -> CHeapObj
     * oop StringTable::intern(oop string, TRAPS) {
     *   if (string == NULL) return NULL;
     *   ResourceMark rm(THREAD);
     *   int length;
     *   Handle h_string (THREAD, string);
     *   jchar* chars = java_lang_String::as_unicode_string(string, length,
     *                                                      CHECK_NULL);
     *   oop result = intern(h_string, chars, length, CHECK_NULL); // 对应的是 oop StringTable::intern(Handle string_or_null_h, const jchar* name, int len, TRAPS)
     *   return result;
     * }
     *
     * oop StringTable::intern(Handle string_or_null_h, const jchar* name, int len, TRAPS) {
     *   // shared table always uses java_lang_String::hash_code
     *   unsigned int hash = java_lang_String::hash_code(name, len); // 将其转换为 hash值
     *   oop found_string = lookup_shared(name, len, hash); //
     *   if (found_string != NULL) {
     *     return found_string;
     *   }
     *   if (_alt_hash) {
     *     hash = hash_string(name, len, true);
     *   }
     *   found_string = do_lookup(name, len, hash);
     *   if (found_string != NULL) {
     *     return found_string;
     *   }
     *   return do_intern(string_or_null_h, name, len, hash, THREAD);
     * }
     * // lookup_shared 实际就是 查找 _shared_table  中的数据, 如果查找不到则会在 _local_table 中进行查找
     * // 最后会执行 oop StringTable::do_intern(Handle string_or_null_h, const jchar* name,int len, uintx hash, TRAPS)
     * oop StringTable::do_intern(Handle string_or_null_h, const jchar* name,
     *                            int len, uintx hash, TRAPS) {
     *   HandleMark hm(THREAD);  // cleanup strings created
     *   Handle string_h;
     *  // 处理当前 string 对象,保证其不为空,如果为空则会创建一个新的对象
     *   if (!string_or_null_h.is_null()) {
     *     string_h = string_or_null_h;
     *   } else {
     *     string_h = java_lang_String::create_from_unicode(name, len, CHECK_NULL);
     *   }
     *
     *   // Deduplicate the string before it is interned. Note that we should never
     *   // deduplicate a string after it has been interned. Doing so will counteract
     *   // compiler optimizations done on e.g. interned string literals.
     *   Universe::heap()->deduplicate_string(string_h()); // 执行重复数据清除,对于当前实现一般都是垃圾回收器来实现,所以在string对象创建之前进行处理
     *
     *   assert(java_lang_String::equals(string_h(), name, len),
     *          "string must be properly initialized");
     *   assert(len == java_lang_String::length(string_h()), "Must be same length");
     *
     *   StringTableLookupOop lookup(THREAD, hash, string_h);
     *   StringTableGet stg(THREAD);
     *
     *   bool rehash_warning;
     *   do {
     *     // Callers have already looked up the String using the jchar* name, so just go to add.
     *     WeakHandle<vm_string_table_data> wh = WeakHandle<vm_string_table_data>::create(string_h); // 创建一个weakHandle对象来存储当前String
     *     // The hash table takes ownership of the WeakHandle, even if it's not inserted.
     *     if (_local_table->insert(THREAD, lookup, wh, &rehash_warning)) { // 并在 _local_table 中写入当前数据,并和当前线程进行关联
     *       update_needs_rehash(rehash_warning);
     *       return wh.resolve();
     *     }
     *     // In case another thread did a concurrent add, return value already in the table.
     *     // This could fail if the String got gc'ed concurrently, so loop back until success.
     *     if (_local_table->get(THREAD, lookup, stg, &rehash_warning)) {
     *       update_needs_rehash(rehash_warning);
     *       return stg.get_res_oop();
     *     }
     *   } while(true);
     * }
     *
     * @param args
     */
    public static void main(String[] args) {
        Class<? super Integer> superclass = int.class.getSuperclass();
        log.info("{}",superclass);
        assert superclass==null;
    }
}
