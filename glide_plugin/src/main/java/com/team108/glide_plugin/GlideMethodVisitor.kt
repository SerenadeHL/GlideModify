package com.team108.glide_plugin

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 文件名：GlideMethodVisitor
 * 作者：Serenade
 * 创建时间：2021-05-21 17:39:43
 * 邮箱：SerenadeHL@163.com
 * 描述：
 */
class GlideMethodVisitor(mv: MethodVisitor) : MethodVisitor(Opcodes.ASM7, mv) {
    override fun visitInsn(opcode: Int) {
        mv.visitInsn(Opcodes.ACONST_NULL)
        mv.visitInsn(Opcodes.ARETURN)
    }

    override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label?, vararg labels: Label?) {
        val list = mutableListOf<Label?>()
        for (i in 0..labels.lastIndex) {
            list.add(dflt)
        }
        super.visitTableSwitchInsn(min, max, dflt, *list.toTypedArray())
    }
}