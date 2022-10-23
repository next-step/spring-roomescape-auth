package nextstep.domain.theme;

import nextstep.domain.Identity;

public class Theme {
    private Identity id;
    private String name;
    private String desc;
    private Long price;

    public Theme(Identity id, String name, String desc, Long price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Identity getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Long getPrice() {
        return price;
    }
}
