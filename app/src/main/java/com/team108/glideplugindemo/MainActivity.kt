package com.team108.glideplugindemo

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url =
            "https://upload.jianshu.io/users/upload_avatars/9513946/1dd3e99b-74d5-4108-b8de-99981417bdf3?imageMogr2/auto-orient/strip|imageView2/1/w/90/h/90/format/webp"
        Glide.with(this)
            .load(url)
            .into(findViewById(R.id.ivImage))

//        val file = File("/sdcard/baidu.png")
//        Log.e("serenade", "uri = ${Uri.fromFile(file)}")
//        Glide.with(this)
//            .load(Uri.fromFile(file))
//            .into(findViewById(R.id.ivImage))
//        Log.e("serenade", "uri = ${Uri.parse("/sdcard/baidu.png")}")

//        Glide.with(this)
//            .load(Uri.parse("content://com.android.contacts/contacts/2/photo"))
//            .into(findViewById(R.id.ivImage))

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_CONTACTS
                ), 100
            )
        }
    }
}