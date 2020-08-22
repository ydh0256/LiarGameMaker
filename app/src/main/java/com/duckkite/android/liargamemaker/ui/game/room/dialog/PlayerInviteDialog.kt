package com.duckkite.android.liargamemaker.ui.game.room.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import com.duckkite.android.liargamemaker.data.model.InviteInfo
import com.duckkite.android.liargamemaker.databinding.DialogInviteBinding
import com.duckkite.android.liargamemaker.util.extention.dp
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

class PlayerInviteDialog(windowContext: Context) : Dialog(windowContext) {

    private val bindingView: DialogInviteBinding
    lateinit var inviteInfo: InviteInfo

    init {
        val layoutInflater = LayoutInflater.from(windowContext)
        bindingView = DialogInviteBinding.inflate(layoutInflater, null, false)
        setContentView(bindingView.root)
    }

    override fun show() {
        val jsonString = Gson().toJson(inviteInfo)
        val mQRBitmap = generateQR(jsonString)
        bindingView.qrCodeImage.setImageBitmap(mQRBitmap)
        super.show()
    }

    inline fun show(func: PlayerInviteDialog.() -> Unit): PlayerInviteDialog {
        this.func()
        this.show()
        return this
    }

    private fun generateQR(value: String) : Bitmap? {
        val bitMatrix: BitMatrix
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.CHARACTER_SET] = CharacterSetECI.UTF8

            bitMatrix = MultiFormatWriter().encode(
                value,
                BarcodeFormat.QR_CODE,
                280.dp,
                280.dp,
                hints
            )
        }catch (illegalargumentexception : IllegalArgumentException){
            return null
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    Color.BLACK
                else
                    Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 280.dp, 0, 0, bitMatrixWidth, bitMatrixHeight)

        return bitmap
    }
}