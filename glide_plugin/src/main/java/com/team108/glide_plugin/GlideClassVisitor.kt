package com.team108.glide_plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.util.concurrent.CopyOnWriteArrayList


/**
 * 文件名：GlideClassVisitor
 * 作者：Serenade
 * 创建时间：2021-05-20 18:25:20
 * 邮箱：SerenadeHL@163.com
 * 描述：
 */
class GlideClassVisitor(cv: ClassVisitor) : ClassVisitor(Opcodes.ASM7, cv) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        //先得到原始方法
        val originMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return when (name) {
            "loadResourceFromUri", "openContactPhotoInputStream" -> {
                println("开始修改 $name 方法")
                GlideMethodVisitor(originMethodVisitor)
            }
            else -> {
                //不需要处理的方法返回原始方法
                originMethodVisitor
            }
        }
    }
}