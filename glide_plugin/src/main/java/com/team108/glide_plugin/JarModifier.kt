package com.team108.glide_plugin

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassReader
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * 文件名：JarVisitor
 * 作者：Serenade
 * 创建时间：2021-05-21 11:18:57
 * 邮箱：SerenadeHL@163.com
 * 描述：
 */
object JarModifier {
    fun modify(file: File, tmpDir: File): File {
        val inputJarFile = JarFile(file)
        val outputJarFile = File(tmpDir, file.name)
        if (outputJarFile.exists()){
            outputJarFile.delete()
        }
        val jarOutputStream = JarOutputStream(FileOutputStream(outputJarFile))
        println("inputJarFile=$file")
        println("outputJarFile=$outputJarFile")
        println("===================================")
        inputJarFile.entries().iterator().forEach { classFileEntry ->
//            var isTarget = false
            val inputStream = inputJarFile.getInputStream(classFileEntry)
            jarOutputStream.putNextEntry(ZipEntry(classFileEntry.name))
            val byteArray =
                if (classFileEntry.name?.endsWith("StreamLocalUriFetcher.class") == true) {
                    println("开始修改 StreamLocalUriFetcher.class")
                    val classReader = ClassReader(inputStream)
                    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val classVisitor = GlideClassVisitor(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
//                    isTarget = true
                    println("修改 StreamLocalUriFetcher.class 结束")
                    classWriter.toByteArray()
                } else {
                    inputStream.readBytes()
                }
            inputStream.close()
            jarOutputStream.write(byteArray)
            jarOutputStream.closeEntry()
//            if (isTarget) {
//                //生成新的Class文件
//                val modifiedClassFile = File(tmpDir, classFileEntry.name)
//                if (!modifiedClassFile.parentFile.exists()) {
//                    modifiedClassFile.parentFile.mkdirs()
//                }
//                if (modifiedClassFile.exists()) {
//                    modifiedClassFile.delete()
//                }
//                modifiedClassFile.createNewFile()
//                val fos = FileOutputStream(modifiedClassFile)
//                fos.write(byteArray)
//                fos.close()
//                println("modifiedClass=$modifiedClassFile")
//            }
        }
        inputJarFile.close()
        jarOutputStream.close()
        return outputJarFile
    }
}