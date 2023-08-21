package school21.spring.service.process;


import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import school21.spring.service.annotations.OrmColumn;
import school21.spring.service.annotations.OrmColumnId;
import school21.spring.service.annotations.OrmEntity;
import school21.spring.service.models.User;


@SupportedAnnotationTypes("school21.spring.service.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class OrmProcessor  extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    StringBuilder fileBuffer = new StringBuilder();
    System.out.println("EExxffFFFFFFF");
    for (Element entity : roundEnv.getElementsAnnotatedWith(OrmEntity.class)) {
      if (entity instanceof TypeElement) {
        OrmEntity ormEntity = entity.getAnnotation(OrmEntity.class);
        fileBuffer.append(String.format("drop table if exists %s;\n", ormEntity.table())).append(String.format("create table if not exists %s (\n", ormEntity.table()));
        for (Element cols : entity.getEnclosedElements()) {
          if (cols.getAnnotation(OrmColumnId.class) != null) {
            OrmColumnId ormColumnId = cols.getAnnotation(OrmColumnId.class);
            fileBuffer.append("\t")
                .append(ormColumnId.id())
                .append(" ")
                .append(ormColumnId.autoincrement());
          }
          if (cols.getAnnotation(OrmColumn.class) != null) {
            try {
            OrmColumn ormColumn = cols.getAnnotation(OrmColumn.class);
            fileBuffer.append(",\n\t")
                .append(ormColumn.name())
                .append(" ")
                .append(typeToSqlType(getFieldType(ormColumn.name()), ormColumn.length()));
            } catch (ClassNotFoundException | NoSuchFieldException e) {
              throw new RuntimeException(e);
            }
          }
        }
      }
      fileBuffer.append("\n);\n");
      createSchema(fileBuffer.toString());
    }
    return true;
  }


  private String getFieldType(String fieldName)
      throws ClassNotFoundException, NoSuchFieldException {
    Class<?> clazz = User.class;
    Field field = clazz.getDeclaredField(fieldName);
    return field.getType().getSimpleName();
  }
  private String typeToSqlType(String fType, int tLength) {
    String toSql;
    if (fType.equals("String"))
      toSql = String.format("VARCHAR(%d)", tLength);
     else
      toSql = fType.toLowerCase();
    return toSql;
  }
  private void createSchema(String schema) {
    try {
      Path outputFile = Paths.get("target/classes", "schema.sql");
      BufferedWriter bfw = Files.newBufferedWriter(outputFile);
      bfw.write(schema);
      bfw.close();
    } catch (IOException e) {
      e.fillInStackTrace();
    }
  }
}
