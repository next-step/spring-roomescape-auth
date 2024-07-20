package roomescape.domain;

import roomescape.exception.ErrorCode;
import roomescape.exception.custom.InvalidInputException;

public class Theme {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 21;
    private static final int MIN_DESCRIPTION_LENGTH = 21;

    private void validateName(String name) {
        if(name.length() < MIN_NAME_LENGTH ||
                name.length() > MAX_NAME_LENGTH){
            throw new InvalidInputException(ErrorCode.INVALID_INPUT,"제목은 3글자 이상 20글자 이하여야합니다.");
        }
    }

    private void validateDescription(String description) {
        if(description.length() < MIN_DESCRIPTION_LENGTH){
            throw new InvalidInputException(ErrorCode.INVALID_INPUT,"설명이 너무 짧습니다. 더 작성해주세요.");
        }
    }

    public Theme(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Theme(String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
