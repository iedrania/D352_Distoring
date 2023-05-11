package com.iedrania.distoring.ui.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.iedrania.distoring.R
import com.iedrania.distoring.databinding.ActivityAddBinding
import com.iedrania.distoring.helper.*
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.camera.CameraActivity
import com.iedrania.distoring.ui.login.LoginActivity
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var token: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, getString(R.string.camera_no_perm), Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        val pref = LoginPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, null, null)
        )[MainViewModel::class.java]
        mainViewModel.getSessionInfo().observe(this) { isLogin ->
            if (isLogin) {
                mainViewModel.getLoginInfo().observe(this) {
                    token = it
                }
            } else {
                val intent = Intent(this@AddActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        mainViewModel.isLoading.observe(this) { showLoading(it) }
        mainViewModel.isError.observe(this) { showError(it) }
        mainViewModel.isFail.observe(this) { showFailure(it) }
        mainViewModel.isSuccess.observe(this) { showSuccess(it) }

        binding.btnAddCamera.setOnClickListener { startCameraX() }
        binding.btnAddGallery.setOnClickListener { startGallery() }
        binding.btnAddUpload.setOnClickListener { uploadImage() }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.edAddDescription.text.toString()
            mainViewModel.postStory(token, file, description)
        } else {
            Toast.makeText(
                this@AddActivity, getString(R.string.no_image_file), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivAddPhotoPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                getFile = uriToFile(uri, this@AddActivity)
                binding.ivAddPhotoPreview.setImageURI(uri)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(
                this@AddActivity, getString(R.string.post_story_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showFailure(isFail: Boolean) {
        if (isFail) {
            Toast.makeText(
                this@AddActivity, getString(R.string.retrofit_fail), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Toast.makeText(this@AddActivity, getString(R.string.story_posted), Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}