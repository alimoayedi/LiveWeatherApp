package amastudio.ali.liveweather.onDeviceFileServices;

import amastudio.ali.liveweather.R;

/**
 * Created by ali on 13/08/2018.
 */

public class GetWeatherImg {        //based on image code, returns image or icon (two methods)

    public int getWeatherImg(String code){
        int fileName;
        switch(Integer.parseInt(code)){
            case 0:
                fileName = R.drawable.img0;
                break;
            case 1:
                fileName = R.drawable.img1;
                break;
            case 2:
                fileName = R.drawable.img2;
                break;
            case 3:
                fileName = R.drawable.img2;
                break;
            case 4:
                fileName = R.drawable.img2;
                break;
            case 5:
                fileName = R.drawable.img5;
                break;
            case 6:
                fileName = R.drawable.img6;
                break;
            case 7:
                fileName = R.drawable.img6;
                break;
            case 8:
                fileName = R.drawable.img8;
                break;
            case 9:
                fileName = R.drawable.img8;
                break;
            case 10:
                fileName = R.drawable.img8;
                break;
            case 11:
                fileName = R.drawable.img11;
                break;
            case 12:
                fileName = R.drawable.img11;
                break;
            case 13:
                fileName = R.drawable.img13;
                break;
            case 14:
                fileName = R.drawable.img13;
                break;
            case 15:
                fileName = R.drawable.img13;
                break;
            case 16:
                fileName = R.drawable.img16;
                break;
            case 17:
                fileName = R.drawable.img16;
                break;
            case 18:
                fileName = R.drawable.img8;
                break;
            case 19:
                fileName = R.drawable.img19;
                break;
            case 20:
                fileName = R.drawable.img19;
                break;
            case 21:
                fileName = R.drawable.img19;
                break;
            case 22:
                fileName = R.drawable.img19;
                break;
            case 23:
                fileName = R.drawable.img1;
                break;
            case 24:
                fileName = R.drawable.img1;
                break;
            case 25:
                fileName = R.drawable.img25;
                break;
            case 26:
                fileName = R.drawable.img26;
                break;
            case 27:
                fileName = R.drawable.img27;
                break;
            case 28:
                fileName = R.drawable.img28;
                break;
            case 29:
                fileName = R.drawable.img27;
                break;
            case 30:
                fileName = R.drawable.img28;
                break;
            case 31:
                fileName = R.drawable.img31;
                break;
            case 32:
                fileName = R.drawable.img32;
                break;
            case 33:
                fileName = R.drawable.img27;
                break;
            case 34:
                fileName = R.drawable.img28;
                break;
            case 35:
                fileName = R.drawable.img8;
                break;
            case 36:
                fileName = R.drawable.img36;
                break;
            case 37:
                fileName = R.drawable.img37;
                break;
            case 38:
                fileName = R.drawable.img37;
                break;
            case 39:
                fileName = R.drawable.img37;
                break;
            case 40:
                fileName = R.drawable.img40;
                break;
            case 41:
                fileName = R.drawable.img16;
                break;
            case 42:
                fileName = R.drawable.img42;
                break;
            case 43:
                fileName = R.drawable.img16;
                break;
            case 44:
                fileName = R.drawable.img28;
                break;
            case 45:
                fileName = R.drawable.img2;
                break;
            case 46:
                fileName = R.drawable.img16;
                break;
            case 47:
                fileName = R.drawable.img37;
                break;
            default:
                fileName = R.drawable.error;
                break;
        }
        return fileName;
    }

    public int getWeatherIcon(String code){
        int fileName;
        switch(Integer.parseInt(code)){
            case 0:
                fileName = R.drawable.img0_s;
                break;
            case 1:
                fileName = R.drawable.img1_s;
                break;
            case 2:
                fileName = R.drawable.img2_s;
                break;
            case 3:
                fileName = R.drawable.img2_s;
                break;
            case 4:
                fileName = R.drawable.img2_s;
                break;
            case 5:
                fileName = R.drawable.img5_s;
                break;
            case 6:
                fileName = R.drawable.img6_s;
                break;
            case 7:
                fileName = R.drawable.img6_s;
                break;
            case 8:
                fileName = R.drawable.img8_s;
                break;
            case 9:
                fileName = R.drawable.img8_s;
                break;
            case 10:
                fileName = R.drawable.img8_s;
                break;
            case 11:
                fileName = R.drawable.img11_s;
                break;
            case 12:
                fileName = R.drawable.img11_s;
                break;
            case 13:
                fileName = R.drawable.img13_s;
                break;
            case 14:
                fileName = R.drawable.img13_s;
                break;
            case 15:
                fileName = R.drawable.img13_s;
                break;
            case 16:
                fileName = R.drawable.img16_s;
                break;
            case 17:
                fileName = R.drawable.img16_s;
                break;
            case 18:
                fileName = R.drawable.img8_s;
                break;
            case 19:
                fileName = R.drawable.img19_s;
                break;
            case 20:
                fileName = R.drawable.img19_s;
                break;
            case 21:
                fileName = R.drawable.img19_s;
                break;
            case 22:
                fileName = R.drawable.img19_s;
                break;
            case 23:
                fileName = R.drawable.img1_s;
                break;
            case 24:
                fileName = R.drawable.img1_s;
                break;
            case 25:
                fileName = R.drawable.img25_s;
                break;
            case 26:
                fileName = R.drawable.img26_s;
                break;
            case 27:
                fileName = R.drawable.img27_s;
                break;
            case 28:
                fileName = R.drawable.img28_s;
                break;
            case 29:
                fileName = R.drawable.img27_s;
                break;
            case 30:
                fileName = R.drawable.img28_s;
                break;
            case 31:
                fileName = R.drawable.img31_s;
                break;
            case 32:
                fileName = R.drawable.img32_s;
                break;
            case 33:
                fileName = R.drawable.img27_s;
                break;
            case 34:
                fileName = R.drawable.img28_s;
                break;
            case 35:
                fileName = R.drawable.img8_s;
                break;
            case 36:
                fileName = R.drawable.img36_s;
                break;
            case 37:
                fileName = R.drawable.img37_s;
                break;
            case 38:
                fileName = R.drawable.img37_s;
                break;
            case 39:
                fileName = R.drawable.img37_s;
                break;
            case 40:
                fileName = R.drawable.img40_s;
                break;
            case 41:
                fileName = R.drawable.img16_s;
                break;
            case 42:
                fileName = R.drawable.img42_s;
                break;
            case 43:
                fileName = R.drawable.img16_s;
                break;
            case 44:
                fileName = R.drawable.img28_s;
                break;
            case 45:
                fileName = R.drawable.img2_s;
                break;
            case 46:
                fileName = R.drawable.img16_s;
                break;
            case 47:
                fileName = R.drawable.img37_s;
                break;
            default:
                fileName = R.drawable.error_s;
                break;
        }
        return fileName;
    }
}
