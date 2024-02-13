package com.bannet.skils.crop.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bannet.skils.R;
import com.bannet.skils.crop.view.PictureSelectorDialog;
import com.bannet.skils.service.GlobalMethods;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;


public abstract class BannerSelectorFragment extends Fragment implements PictureSelectorDialog.OnSelectedListener {



    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    public String file_name_skill;
    /**
     * 相册选图标记
     */
    private static final int GALLERY_REQUEST_CODE = 0;
    /**
     * 相机拍照标记
     */
    private static final int CAMERA_REQUEST_CODE = 1;
    /**
     * 拍照临时图片
     */
    private String mTempPhotoPath;
    /**
     * 剪切后图像文件
     */
    private Uri mDestination;

    /**
     * 选择提示 PopupWindow
     */
    private PictureSelectorDialog mSelectPictureDialog;
    /**
     * 图片选择的监听回调
     */
    private BannerSelectorFragment.OnPictureSelectedListener mOnPictureSelectedListener;
    public Context context;

    String file_name = "",path;
    File finalFile;

    /**
     * 剪切图片
     */
    protected void selectPicture() {
        mSelectPictureDialog.show(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDestination = Uri.fromFile(new File(getContext().getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        mSelectPictureDialog = PictureSelectorDialog.getInstance();
        mSelectPictureDialog.setOnSelectedListener(this);

        context = getActivity();
    }

    @Override
    public void OnSelected(View v, int position) {
        switch (position) {
            case 0:
                // "拍照"按钮被点击了
                takePhoto();
                break;
            case 1:
                // "从相册选择"按钮被点击了
                pickFromGallery();
                break;
            case 2:
                // "取消"按钮被点击了
                mSelectPictureDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
                pickFromGallery();
            } else if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION) {
                takePhoto();
            }
        }
    }

    private void takePhoto() {
        mSelectPictureDialog.dismiss();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    getString(R.string.Permissin_is_denied),
//                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
//
//
//        } else {
//            mSelectPictureDialog.dismiss();
//
////            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            startActivityForResult(cameraIntent, 1);
//
//        }
    }

    private void pickFromGallery() {

        mSelectPictureDialog.dismiss();
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
//                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
//                    getString(R.string.Permissin_is_denied),
//                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
//        } else {
////            mSelectPictureDialog.dismiss();
////            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
////            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
////            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
////            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 调用相机拍照
                case CAMERA_REQUEST_CODE:
                    Log.e("camera","camera");
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(context, photo);
                    finalFile = new File(getRealPathFromURI(tempUri));
                    Log.e("path case 0 :",finalFile+" siva");
                    file_name = finalFile.getName();
                    path = finalFile+"";
                    File temp = new File(path);
                    startCropActivity(Uri.fromFile(temp));
                    break;
                // 直接从相册获取
                case GALLERY_REQUEST_CODE:
                    Log.e("file","file");
                    startCropActivity(data.getData());
                    break;
                // 裁剪图片结果
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
                // 裁剪图片错误
                case UCrop.RESULT_ERROR:
                    handleCropError(data);
                    break;
                default:
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    /**
     * 裁剪图片方法实现
     *
     * @param source
     */
    public void startCropActivity(Uri source) {
        Log.e("source",source+"");
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(getResources().getColor(R.color.appcolour));
        // 修改状态栏颜色
        options.setStatusBarColor(getResources().getColor(R.color.appcolour));
        // 隐藏底部工具
        options.setHideBottomControls(true);
        // 图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置图片压缩质量
        options.setCompressionQuality(100);

        UCrop.of(source, mDestination)
                // 长宽比
                .withAspectRatio(16, 9)
                // 图片大小
                .withMaxResultSize(2000, 2000)
                // 配置参数
                .withOptions(options)
                .start(getContext(), this);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri && null != mOnPictureSelectedListener) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOnPictureSelectedListener.onPictureSelected(resultUri, bitmap);
        } else {

        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {


        } else {

        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /**
     * 请求权限
     * <p>
     * 如果权限被拒绝过，则提示用户需要权限
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showAlertDialog(getString(R.string.Permissin_is_denied), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.ok), null, getString(R.string.cancel));
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }




    /**
     * 显示指定标题和信息的对话框
     *
     * @param title                         - 标题
     * @param message                       - 信息
     * @param onPositiveButtonClickListener - 肯定按钮监听
     * @param positiveText                  - 肯定按钮信息
     * @param onNegativeButtonClickListener - 否定按钮监听
     * @param negativeText                  - 否定按钮信息
     */
    protected void showAlertDialog(String title, String message,
                                   DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   String positiveText,
                                   DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }

    /**
     * 设置图片选择的回调监听
     *
     * @param l
     */
    public void setOnPictureSelectedListener(BannerSelectorFragment.OnPictureSelectedListener l) {
        this.mOnPictureSelectedListener = l;
    }

    /**
     * 图片选择的回调接口
     */
    public interface OnPictureSelectedListener {
        /**
         * 图片选择的监听回调
         *
         * @param fileUri
         * @param bitmap
         */
        void onPictureSelected(Uri fileUri, Bitmap bitmap);
    }
}