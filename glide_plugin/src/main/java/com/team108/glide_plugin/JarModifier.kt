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
        if (outputJarFile.exists()) {
            outputJarFile.delete()
        }
        val jarOutputStream = JarOutputStream(FileOutputStream(outputJarFile))
        println("inputJarFile=$file")
        println("outputJarFile=$outputJarFile")
        println("===================================")
        inputJarFile.entries().iterator().forEach { classFileEntry ->
            val inputStream = inputJarFile.getInputStream(classFileEntry)
            jarOutputStream.putNextEntry(ZipEntry(classFileEntry.name))
            val byteArray =
                if (classFileEntry.name?.endsWith("StreamLocalUriFetcher.class") == true) {
                    println("开始修改 StreamLocalUriFetcher.class")
                    val classReader = ClassReader(inputStream)
                    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val classVisitor = GlideClassVisitor(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    println("修改 StreamLocalUriFetcher.class 结束")
                    classWriter.toByteArray()
                } else {
                    inputStream.readBytes()
                }
            inputStream.close()
            jarOutputStream.write(byteArray)
            jarOutputStream.closeEntry()
        }
        inputJarFile.close()
        jarOutputStream.close()

        printJarInfo(file)
        printJarInfo(outputJarFile)

        return outputJarFile
    }

    private fun printJarInfo(file: File) {
        println()
        println("**************************************************************")
        println("******                                                  ******")
        println("******               Print Jar Info Start               ******")
        println("******                                                  ******")
        println("**************************************************************")
        println()

        println("jar file path:$file")
        val jarFile = JarFile(file)
        println("jar class num:${jarFile.entries().toList().size}")

        println("===================================printAllClass Start===================================")
        jarFile.entries().iterator().forEach {
            println(it.name)
        }
        println("===================================printAllClass End===================================")

        println()
        println("**************************************************************")
        println("******                                                  ******")
        println("******               Print Jar Info End                 ******")
        println("******                                                  ******")
        println("**************************************************************")
        println()
    }
}