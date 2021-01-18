package github.andreepdias.fitnessmanager.model.entity.enums;

public enum Unit {
    UNIT(1, "un."),
    GRAM(2, "g"),
    ML(3, "ml");

    private int code;
    private String description;

    private Unit(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Unit toEnum(Integer code) {
        if(code == null) {
            return null;
        }
        for (Unit x : Unit.values()) {
            if(code.equals(x.getCode())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid id: " + code + " for enum Unit." );
    }

    public static Unit toEnum(String description) {
        if(description == null) {
            return null;
        }
        for (Unit x : Unit.values()) {
            if(description.equals(x.getDescription())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid description: " + description + " for enum Unit." );
    }

    public int getCode(){
        return code;
    }

    public String getDescription() {
        return description;
    }

}
