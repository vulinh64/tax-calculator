package com.vulinh.utils;

import java.util.Objects;
import lombok.*;
import org.apache.poi.ss.usermodel.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellBuilder {

  private CellStyle thinStyle;
  private Font defaultFont;
  private short defaultDataFormat = -1;

  private final Workbook workbook;
  private final Sheet sheet;

  public static CellBuilder init(@NonNull Workbook workbook, @NonNull Sheet sheet) {
    return new CellBuilder(workbook, sheet);
  }

  public Cell newCell(int rowNumber, int cellNumber) {
    var cell = sheet.getRow(rowNumber).createCell(cellNumber);

    cell.setCellStyle(thinCellStyle());

    return cell;
  }

  private CellStyle thinCellStyle() {
    if (Objects.isNull(thinStyle)) {
      thinStyle = workbook.createCellStyle();
      thinStyle.setBorderBottom(BorderStyle.THIN);
      thinStyle.setBorderTop(BorderStyle.THIN);
      thinStyle.setBorderLeft(BorderStyle.THIN);
      thinStyle.setBorderRight(BorderStyle.THIN);
      thinStyle.setFont(defaultFont());
      thinStyle.setAlignment(HorizontalAlignment.CENTER);
      thinStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      thinStyle.setDataFormat(defaultDataFormat());
    }

    return thinStyle;
  }

  private Font defaultFont() {
    if (Objects.isNull(defaultFont)) {
      defaultFont = workbook.createFont();
      defaultFont.setFontHeightInPoints((short) 15);
    }

    return defaultFont;
  }

  private short defaultDataFormat() {
    if (defaultDataFormat == -1) {
      defaultDataFormat = workbook.createDataFormat().getFormat("#,##0");
    }

    return defaultDataFormat;
  }
}
