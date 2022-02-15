package com.unipay.android

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.unipay.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var codeScanner: CodeScanner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
    }

    private fun init() {
        binding?.includeToolbar?.tvToolbarTitle?.apply {
            text = "UniPay"
            setTextColor(Color.parseColor("#ffffff"))
        }
        binding?.includeToolbar?.ivToolbarBack?.visibility = View.INVISIBLE


        loadScanner()
        //handleEvents()
    }

    private fun loadScanner() {

        val scannerView = binding?.scannerView
        codeScanner = scannerView?.let { CodeScanner(this, it) }!!

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                handleEvents(it.text.toString())
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun scanSuccess(uri: String) {
        //val UPI = "upi://pay?pa=8547917584@okbizaxis&pn=Prabha Pooja Store&mc=5943&aid=uGICAgIDtrYDsWQ&tr=BCR2DN6T2WD2FQTC"

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(uri)
        val chooser = Intent.createChooser(intent, "Pay with...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(chooser, 1, null)
        }
        startActivityForResult(chooser, 1, null)
    }

    private fun handleEvents(uri: String) {
        binding?.cvGpay?.setOnClickListener {
            val intentGpay =
                packageManager.getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user")
            intentGpay?.data = Uri.parse(uri)
            startActivity(intentGpay)

        }
        binding?.cvPaytm?.setOnClickListener {
            val intentPaytm = packageManager.getLaunchIntentForPackage("net.one97.paytm")

        }
        binding?.cvPhonePe?.setOnClickListener {
            val intentPhonepe = packageManager.getLaunchIntentForPackage("com.phonepe.app")

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