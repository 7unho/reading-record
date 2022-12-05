package item2;

public class NutritionFactsByTeleScoping {
    private final int servingSize; // 필수, (ml, 1회 제공량)
    private final int servings; // 필수, (회, 총 n회 제공량)
    private final int calories; // 선택, (1회 제공량 당 )
    private final int fat; // 선택, (g/1회 제공량)
    private final int sodium; // 선택, (mg/1회 제공량)
    private final int carbohydrate; // 선택, (g/1회 제공량 )

    public NutritionFactsByTeleScoping(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFactsByTeleScoping(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFactsByTeleScoping(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFactsByTeleScoping(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFactsByTeleScoping(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
