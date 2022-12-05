package item2;

public class NutrituinFactsMain {
    public static void main(String[] args) {
        NutritionFactsByBuilderPattern cocaCola = new NutritionFactsByBuilderPattern.Builder(240, 80) // 필수 매개변수
                                                                                    .calories(100)
                                                                                    .sodium(35).
                                                                                    carbohydrate(27).build();

    }
}
