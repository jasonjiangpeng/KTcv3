#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <android/log.h>
#define  LOGSHOW(X)  __android_log_print(ANDROID_LOG_INFO,X,"PRINTRESULT:::");
// TODO
using namespace cv;
using namespace std;
extern "C" {
void denoising(Mat& mat, Mat& result) {
    Mat matc = mat.clone();
    result.setTo(Scalar(0, 0, 0, 0));
    Mat temp;
    temp.create(mat.rows + 2, mat.cols + 2, mat.type());
    int flags = 8 | FLOODFILL_MASK_ONLY | FLOODFILL_FIXED_RANGE;
    for (int h = 0; h < mat.rows; h++) {
        for (int w = 0; w < mat.cols; w++) {
            if (matc.at<uchar>(h, w) == 255) {
                temp.setTo(Scalar(0, 0, 0, 0));
                floodFill(matc, temp, cv::Point(w, h), Scalar(255, 255, 255, 255), 0, Scalar(0, 0, 0, 0), Scalar(0, 0, 0, 0), flags);
                if (countNonZero(temp) > 6000){
                    result.setTo(Scalar(255, 255, 255, 255), temp(Rect(1, 1, mat.cols, mat.rows)));
                }
                matc.setTo(Scalar(0, 0, 0, 0), temp(Rect(1, 1, mat.cols, mat.rows)));
            }
        }
    }
}
JNIEXPORT jint JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_getDifPoint(JNIEnv *env, jclass type, jlong v1, jlong v2) {
Mat *mat1=(Mat*)v1;
    Mat *mat2=(Mat*)v2;
    int  rows=(*mat1).rows;
    int  cols=(*mat1).cols;
    int  dt=(*mat1).type();
    Mat mat3(Size(rows, cols), dt);
    absdiff(*mat1,*mat2,mat3);
    threshold(mat3, mat3, 80, 255, 0);
    
 /*   Mat result;
    result.create(rows, cols, dt);
    denoising(mat3,result);*/
  jint  count=countNonZero(mat3);
    return  count;
}



JNIEXPORT jlong JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_convolution(JNIEnv *env, jclass type, jlong v1,
                                                  jintArray v_) {
    jint *b = env->GetIntArrayElements(v_, NULL);
    Mat  *mat=(Mat*)v1;
    int  rows=(*mat).rows;
    int  cols=(*mat).cols;
    int  ntype=(*mat).type();
    static  Mat  mat2(Size(cols, rows), ntype);
   for (int i = 2; i < rows - 2; i++)
    {
        for (int j = 2; j < cols - 2; j++)
        {
            int a = (*mat).at<uchar>(i - 1, j - 1);
            int a1 = (*mat).at<uchar>(i, j - 1);
            int a2 = (*mat).at<uchar>(i + 1, j - 1);
            int ba = (*mat).at<uchar>(i - 1, j);
            int ba1 = (*mat).at<uchar>(i, j);
            int ba2 = (*mat).at<uchar>(i + 1, j);
            int ca = (*mat).at<uchar>(i - 1, j + 1);
            int ca1 = (*mat).at<uchar>(i, j - 1);
            int ca2 = (*mat).at<uchar>(i + 1, j + 1);
            int  result = a*b[0] + a1*b[1] + a2*b[2] + ba*b[3] + ba1*b[4] + ba2*b[5] + ca*b[6] + ca1*b[7] + ca2*b[8];
            if (result >= 0) {
                result = 255;
            }
            if (result < 0) {
                result = 0;
            }
            mat2.at<uchar>(i, j) = result;
        }
    }

    return (jlong)&mat2;
  }
JNIEXPORT jdouble JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_compareSimilar(JNIEnv *env, jclass type, jlong v1, jlong v2) {
    // TODO
    Mat *mat1 = (Mat *) v1;
    Mat *mat2 = (Mat *) v2;
    int  r=(*mat1).rows;
    int  c=(*mat1).cols;
    double  a = 0;
    double  sum = r*c;
    for (size_t i = 0; i < r; i++)
    {
        for (size_t j = 0; j < c; j++)
        {
            uchar a1 = (*mat1).at<uchar>(i, j);
            uchar a2 = (*mat2).at<uchar>(i, j);
            if (a1 == a2) a++;

        }
    }
    return a/sum;
}

extern "C"
JNIEXPORT jint JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_nCompareHist(JNIEnv *env, jclass type, jlong v1, jlong v2) {
Mat*  mat1=(Mat*)v1;
Mat*  mat2=(Mat*)v2;
    Mat  out1,out2,com1,com2;
    float h_ranges[] = { 0,256 };
    float s_ranges[] = { 0,256 };
    const float*ranges[] = { h_ranges,s_ranges };
    int h_bins = 50;
    int s_bins = 60;
    int histSize[] = { h_bins,s_bins };
    int channels[] = { 0,1 };
    cvtColor(*mat1, out1, COLOR_BGR2HSV);
    cvtColor(*mat2, out2, COLOR_BGR2HSV);
    calcHist(&out1, 1, channels, Mat(), com1, 2, histSize, ranges, true, false);
    normalize(com1, com1, 0, 1, NORM_MINMAX, -1, Mat());
    calcHist(&out2 ,1, channels, Mat(), com2, 2, histSize, ranges, true, false);
    normalize(com2, com2, 0, 1, NORM_MINMAX, -1, Mat());
    double  v=	compareHist(com1,com2,0);
    int  va=v*100;
    return va;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_equalizeHist(JNIEnv *env, jclass type, jlong v1) {
  Mat* mat=(Mat*)v1;
    Mat imageRGB[3];

    split(*mat, imageRGB);
    for (int i = 0; i < 3; i++)
    {
        equalizeHist(imageRGB[i], imageRGB[i]);
    }
    merge(imageRGB, 3, *mat);
    return (jlong)&mat;
}
}

float gett(Mat m1,Mat ma2) {
    float  a = 0;
    for (size_t i = 0; i < m1.rows; i++)
    {
        for (size_t j = 0; j < m1.cols; j++)
        {
            uchar  u = m1.at<uchar>(i, j);
            uchar  u2 = ma2.at<uchar>(i, j);
            if (u == u2) {
                a++;
            }
        }
    }
    float  s = m1.rows*ma2.cols;
    return a / s;
}
extern "C"

JNIEXPORT jfloat JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_charCompare(JNIEnv *env, jclass type, jlong v1, jlong v2) {

    // TODO
    Mat*  mat1=(Mat*)v1;
    Mat*  mat2=(Mat*)v2;
    Mat sobelx1, sobelx2;
    Sobel(*mat1, sobelx1, CV_32F, 1, 0);
    Sobel(*mat2, sobelx2, CV_32F, 1, 0);
    jfloat  a = 0;
    int rows=(*mat1).rows;
    int cols=(*mat1).cols;
    for (size_t i = 0; i < rows; i++)
    {
        for (size_t j = 0; j < cols; j++)
        {
            uchar  u = (*mat1).at<uchar>(i, j);
            uchar  u2 = (*mat2).at<uchar>(i, j);
            if (u == u2) {
                a++;
            }
        }
    }
    jfloat  s = rows*cols;
    return a / s;

}
 extern "C"
JNIEXPORT jlong JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_lplsChange(JNIEnv *env, jclass type, jlong v1) {
    Mat kernelY =Mat::zeros(Size(3,3),CV_8UC1);
    Mat* vsd=(Mat*)v1;
    kernelY.at<float>(0,0)=0;

    kernelY.at<float>(0,1)=-1;

    kernelY.at<float>(0,2)=0;

    kernelY.at<float>(1,0)=0;

    kernelY.at<float>(1,1)=7;

    kernelY.at<float>(1,2)=0;

    kernelY.at<float>(2,0)=-1;

    kernelY.at<float>(2,1)=-2;

    kernelY.at<float>(2,2)=-1;

}
extern "C"
#define  THREVALUE 100
JNIEXPORT jdouble JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_convoSimilar(JNIEnv *env, jclass type, jlong v1, jlong v2) {
    Mat *mat1 = (Mat *) v1;
    Mat *mat2 = (Mat *) v2;
    int  r=(*mat1).rows;
    int  c=(*mat1).cols;
    double  a = 0;
    double  sum = r*c;
    for (size_t i = 0; i < r; i++)
    {
        for (size_t j = 0; j < c; j++)
        {
            uchar a1 = (*mat1).at<uchar>(i, j);
            uchar a2 = (*mat2).at<uchar>(i, j);
            int  rst=a1-a2;
            int  rst2=rst>0?rst:-rst;
            if (rst2>THREVALUE){
                a++;
            }


        }
    }
    return a;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_opencv_zhongke_ktcv_ndk_JniEnter_getDifPoint2(JNIEnv *env, jclass type, jlong v1, jlong v2 ) {
     Mat *mat1=(Mat*)v1;
    Mat *mat2=(Mat*)v2;
    int  rows=(*mat1).rows;
    int  cols=(*mat1).cols;
    int  dt=(*mat1).type();
    static Mat mat3=Mat::zeros(Size(rows,cols),dt);
    absdiff(*mat1,*mat2,mat3);
  //  threshold(mat3, mat3, va, 255, 0);
     return (jlong)&mat3;
}
