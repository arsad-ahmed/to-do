package com.example.todo.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todo.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity:AppCompatActivity()
{


    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            {

                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),1);

            }
            else
            {
                // repeat the permission or open app details
            }
        }
    }
}