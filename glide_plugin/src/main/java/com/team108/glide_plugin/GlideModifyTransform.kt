package com.team108.glide_plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project
import java.io.File


/**
 * 文件名：GlideModifyTransform
 * 作者：Serenade
 * 创建时间：2021-05-20 14:22:27
 * 邮箱：SerenadeHL@163.com
 * 描述：
 */
class GlideModifyTransform(private val mProject: Project) : Transform() {
    override fun getName(): String {
        return GlideModifyTransform::class.java.simpleName
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
//        return mutableSetOf(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    private fun printTransformStart() {
        println()
        println("********************************************************************************")
        println("******                                                                    ******")
        println("******               欢迎使用 GlideModifyTransform 执行开始               ******")
        println("******                                                                    ******")
        println("********************************************************************************")
        println()
    }

    private fun printTransformEnd() {
        println()
        println("********************************************************************************")
        println("******                                                                    ******")
        println("******               欢迎使用 GlideModifyTransform 执行结束               ******")
        println("******                                                                    ******")
        println("********************************************************************************")
        println()
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        printTransformStart()
        val outputProvider = transformInvocation!!.outputProvider
        val tmpDir = transformInvocation.context.temporaryDir
        transformInvocation.inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                //处理Jar
                processJarInput(jarInput, outputProvider, tmpDir)
            }
            input.directoryInputs.forEach { directoryInput ->
                //处理源码文件
                processDirectoryInputs(directoryInput, outputProvider, tmpDir)
            }
        }
        printTransformEnd()
    }

    private fun processJarInput(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider,
        tmpDir: File
    ) {
        val dest = outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        val src = if (jarInput.file.name.contains("glide", true)) {
            println("找到了glide.jar 开始修改")
            JarModifier.modify(jarInput.file, tmpDir)
        } else {
            jarInput.file
        }
        FileUtils.copyFile(src, dest)
    }

    private fun processDirectoryInputs(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider,
        tmpDir: File
    ) {
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        // 将 input 的目录复制到 output 指定目录
        FileUtils.copyDirectory(directoryInput.file, dest)
    }
}