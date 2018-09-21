package com.luiseduardovelaruiz.hopping

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.graphics.SurfaceTexture
import android.os.HandlerThread
import android.support.v7.app.AppCompatActivity
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Surface
import android.view.TextureView
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.toast
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class Camera : AppCompatActivity() {

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            println("onSurfaceTextureAvailable ...")
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit

    }//end surfaceTextureListener

    private lateinit var cameraId: String
    private lateinit var textureView: TextureView

    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null

    private var stateCallback = object: CameraDevice.StateCallback() {
        override fun onOpened(camDevice: CameraDevice?) {
            cameraDevice = camDevice
            createCameraPreviewSession()
            println("ON OPENED")
        }//end onOpened

        override fun onDisconnected(camDevice: CameraDevice?) {
            camDevice?.close()
            cameraDevice = null
        }//end onDisconnected

        override fun onError(camDevice: CameraDevice?, error: Int) {
            onDisconnected(camDevice)
        }//end onError
    }//end stateCallback

    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null

    private var imageReader: ImageReader? = null
    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
    }//end onImageAvailableListener

    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private lateinit var previewRequest: CaptureRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        textureView = camera_activity_textureView

        println("Camera.kt onCreate")
    }//end onCreate


    private fun setUpCameraOutputs() {
        val manager =  getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                // We don't use a front facing camera in this sample.
                val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (cameraDirection != null && cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }

                // For still image captures, we use the largest available size.
                imageReader = ImageReader.newInstance(100, 100, ImageFormat.JPEG,2).apply {
                    setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)
                }

                this.cameraId = cameraId

                // We've found a viable camera and finished setting up member variables,
                // so we don't need to iterate through other available cameras.
                return
            }
        } catch (e: CameraAccessException) {
        } catch (e: NullPointerException) {
        }

    }//end setUpCameraOutputs

    private fun openCamera(){
        /*
         *  Devices running Android 6.0 (API 23) or above will not have permissions set by installing the app,
         *  it is necessary to ask for permissions during runtime
         */
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            setUpCameraOutputs()
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                manager.openCamera(cameraId, stateCallback, backgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
        if (permission == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Conceder Permiso")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), PackageManager.PERMISSION_GRANTED)
            }
        }
    }//end openCamera

    fun createCameraPreviewSession() {
        try {
            val texture = textureView.surfaceTexture

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(1000, 1000)

            // This is the output Surface we need to start preview.
            val surface = Surface(texture)

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(surface)

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface), object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // The camera is already closed
                            if (cameraDevice == null) {
                                println("SOMEHOW CAMERA DEVICE IS NULL")
                                return
                            }

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build()
                                captureSession?.setRepeatingRequest(previewRequest, captureCallback, backgroundHandler)
                            } catch (e: CameraAccessException) {
                            }

                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            toast("failed")
                        }
                    }, null)
        } catch (e: CameraAccessException) {
        }
    }//end createCameraPreviewSession

    override fun onResume() {
        super.onResume()
        if (textureView.isAvailable) {
            openCamera()
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
        println("Camera.kt onResume")
    }//end onResume

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
    }
}//end class