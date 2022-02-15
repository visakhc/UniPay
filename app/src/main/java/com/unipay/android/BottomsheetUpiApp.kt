package com.unipay.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.unipay.android.databinding.BottomSheetUpiAppBinding

class BottomsheetUpiApp(upiId: String): BottomSheetDialogFragment(){
    private var binding: BottomSheetUpiAppBinding? = null
    private var mUpiId = upiId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetUpiAppBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        handleEvents()
    }

    private fun handleEvents() {
        binding?.cvGpay?.setOnClickListener {
            if (mUpiId.isEmpty()) {
                binding?.let { Snackbar.make(it.root, "Scan QR code to pay", Snackbar.LENGTH_SHORT).show() }
            } else {
                val PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
                startPayment(PACKAGE_NAME)
            }
        }
        binding?.cvPaytm?.setOnClickListener {
            if (mUpiId.isEmpty()) {
                binding?.let { Snackbar.make(it.root, "Scan QR code to pay", Snackbar.LENGTH_SHORT).show() }
            } else {
                val PACKAGE_NAME = "net.one97.paytm"
                startPayment(PACKAGE_NAME)
            }
        }
        binding?.cvPhonePe?.setOnClickListener {
            if (mUpiId.isEmpty()) {
                binding?.let { Snackbar.make(it.root, "Scan QR code to pay", Snackbar.LENGTH_SHORT).show() }
            } else {
                val PACKAGE_NAME = "com.phonepe.app"
                startPayment(PACKAGE_NAME)
            }
        }
    }

    private fun startPayment(packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(mUpiId)
        intent.setPackage(packageName)
        startActivityForResult(intent, 123)
    }
}