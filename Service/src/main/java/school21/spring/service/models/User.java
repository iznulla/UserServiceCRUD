package school21.spring.service.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import school21.spring.service.annotations.OrmColumn;
import school21.spring.service.annotations.OrmColumnId;
import school21.spring.service.annotations.OrmEntity;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@OrmEntity(table = "Users")
public class User {
  @OrmColumnId
  private Long identifier;

  @OrmColumn(name = "email", length = 128)
  @NonNull
  private String email;

  @OrmColumn(name = "password", length = 128)
  @NonNull
  private String password;
}
