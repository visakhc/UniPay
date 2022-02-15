package com.unipay.android

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import com.unipay.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var codeScanner: CodeScanner
    private var mUpiId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.includeToolbar?.tvToolbarTitle?.apply {
            text = "UniPay"
            setTextColor(Color.parseColor("#ffffff"))
        }
        binding?.includeToolbar?.ivToolbarBack?.visibility = View.INVISIBLE

        init()
    }

    private fun init() {
        permissonChecker()
        handleEvents()
    }


    private fun handleEvents() {

    }

    private fun permissonChecker() {
        PermissionX.init(this@MainActivity)
            .permissions(Manifest.permission.CAMERA)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    loadScanner()
                } else {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun loadScanner() {
        val scannerView = binding?.scannerView
        codeScanner = scannerView?.let { CodeScanner(this, it) }!!
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                mUpiId = it.text.toString()
                BottomsheetUpiApp(mUpiId).show(supportFragmentManager,"uiBottomSheet")
            }
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "error",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }



    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}