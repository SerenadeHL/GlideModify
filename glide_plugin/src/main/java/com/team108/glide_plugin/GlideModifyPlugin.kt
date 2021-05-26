package com.team108.glide_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 文件名：GlideModifyPlugin
 * 作者：Serenade
 * 创建时间：2021-05-20 14:35:15
 * 邮箱：SerenadeHL@163.com
 * 描述：
 */
class GlideModifyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val appExtension = project.extensions.findByType(AppExtension::class.java)
        appExtension?.registerTransform(GlideModifyTransform(project))
    }
}