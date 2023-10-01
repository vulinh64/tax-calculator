package com.vulinh.utils;

import java.util.Objects;
import lombok.*;
import org.apache.poi.ss.usermodel.*;

public class CellBuilder implements AutoCloseable {

  private final int sheetIndex;

  private Workbook workbook;

  private CellStyle cellStyle;
  private Font font;

  private short defaultDataFormat = -1;

  public static CellBuilder init(@NonNull Workbook workbook, int sheetIndex) {
    return new CellBuilder(workbook, sheetIndex);
  }

  public Cell newCell(int rowNumber, int cellNumber) {
    var sheet = workbook.getSheetAt(sheetIndex);

    var cell = sheet.getRow(rowNumber).createCell(cellNumber);

    cell.setCellStyle(thinCellStyle());

    return cell;
  }

  public CellBuilder(Workbook workbook, int sheetIndex) {
    this.workbook = workbook;
    this.sheetIndex = sheetIndex;
  }

  private CellStyle thinCellStyle() {
    if (Objects.isNull(cellStyle)) {
      cellStyle = workbook.createCellStyle();
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setFont(defaultFont());
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setDataFormat(defaultDataFormat());
    }

    return cellStyle;
  }

  private Font defaultFont() {
    if (Objects.isNull(font)) {
      font = workbook.createFont();
      font.setFontHeightInPoints((short) 15);
    }

    return font;
  }

  private short defaultDataFormat() {
    if (defaultDataFormat == -1) {
      defaultDataFormat = workbook.createDataFormat().getFormat("#,##0");
    }

    return defaultDataFormat;
  }

  @Override
  public void close() {
    workbook = null;
    font = null;
    cellStyle = null;
  }
}
