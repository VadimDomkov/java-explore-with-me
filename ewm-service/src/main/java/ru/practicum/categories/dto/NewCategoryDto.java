package ru.practicum.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotBlank
    @Length(max = 50)
    private String name;
}
