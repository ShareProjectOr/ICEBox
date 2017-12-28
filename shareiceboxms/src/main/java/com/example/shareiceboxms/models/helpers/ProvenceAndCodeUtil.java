package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.shareiceboxms.models.beans.address.ItemArea;
import com.example.shareiceboxms.models.beans.address.ItemCity;
import com.example.shareiceboxms.models.beans.address.ItemProvence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by WH on 2017/7/22.
 * 需要导包：jxl.jar
 */

public class ProvenceAndCodeUtil {
    private static ProvenceAndCodeUtil instance;
    private static String NAME = "provence_code_excel.xls";
    private static List<ItemProvence> provences;

    public static ProvenceAndCodeUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ProvenceAndCodeUtil();
        }
        try {
            provences = instance.readFromExcel(context);
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static List<ItemProvence> getProvences() {
        return provences;
    }

    /*
        * name:文件名
        * */
    public List<ItemProvence> readFromExcel(Context context) throws BiffException, IOException {
        AssetManager manager = context.getAssets();
        String lastProvenceName = "";
        Workbook workbook = Workbook.getWorkbook(manager.open(NAME));
        Sheet sheet = workbook.getSheet(0);
        int rows = sheet.getRows();
        List<ItemProvence> provences = new ArrayList<>();
        for (int i = 1; i < rows - 1; i++) {
            String content = sheet.getCell(7, i).getContents();
            String[] ceilArray = content.split(",");
            Log.d("debug", "content=" + content);
            if (ceilArray.length == 1) {
                continue;
            }
            if (ceilArray.length == 2) {
                if (!ceilArray[1].equals(lastProvenceName)) {
                    ItemProvence provence = new ItemProvence();
                    String id = sheet.getCell(0, i).getContents();
                    String parentId = sheet.getCell(2, i).getContents();
                    provence.setName(ceilArray[1]);
                    provence.setId(id);
                    provence.setParentId(parentId);
                    provences.add(provence);
                    lastProvenceName = ceilArray[1];
                }
                continue;
            }
            int provenceSize = provences.size();
            ItemProvence provence = null;
            if (provenceSize == 0) {
                provence = provences.get(0);
            } else {
                provence = provences.get(provenceSize - 1);
            }
            List<ItemCity> citys = provence.getCites();
            if (citys == null) {
                citys = new ArrayList<>();
                provence.setCites(citys);
            }
            if (ceilArray.length == 3) {
                ItemCity city = new ItemCity();
                String id = sheet.getCell(0, i).getContents();
                String parentId = sheet.getCell(2, i).getContents();
                String cityCode = sheet.getCell(1, i).getContents();
                city.setName(ceilArray[2]);
                city.setId(id);
                city.setParentId(parentId);
                city.setCityCode(cityCode);
                provence.getCites().add(city);
            }
            int citySize = citys.size();
            ItemCity city = null;
            if (citySize == 0) {
                city = provence.getCites().get(0);
            } else {
                city = provence.getCites().get(citySize - 1);
            }
            List<ItemArea> areas = city.getAreas();
            if (areas == null) {
                areas = new ArrayList<>();
                city.setAreas(areas);
            }
            if (ceilArray.length == 4) {
                String id = sheet.getCell(0, i).getContents();
                String parentId = sheet.getCell(2, i).getContents();
                String cityCode = sheet.getCell(1, i).getContents();
                String zipCode = sheet.getCell(2, i).getContents();
                String areaName = ceilArray[3];
                ItemArea area = new ItemArea();
                area.setId(id);
                area.setParentId(parentId);
                area.setCityCode(cityCode);
                area.setName(areaName);
                area.setZipCode(zipCode);
                city.getAreas().add(area);
            }
        }
        return provences;
    }
}
