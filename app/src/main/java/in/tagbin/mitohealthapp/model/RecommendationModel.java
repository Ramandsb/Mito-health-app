package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 10/9/16.
 */
public class RecommendationModel {
    String meal_time;
    MealTypeModel meal_type;
    List<MealsModel> meals;
    public RecommendationModel(){
        meal_type = new MealTypeModel();
        meals = new ArrayList<MealsModel>();
    }
    public class MealTypeModel{
        int id;
        String food_time;

        public int getId() {
            return id;
        }

        public String getFood_time() {
            return food_time;
        }
    }

    public List<MealsModel> getMeals() {
        return meals;
    }

    public MealTypeModel getMeal_type() {
        return meal_type;
    }

    public String getMeal_time() {
        return meal_time;
    }

    public class MealsModel {
        ComponentModel component;
        int id,meal_id,flag;
        float amount;
        String type, time;

        public MealsModel() {
            component = new ComponentModel();
        }

        public int getMeal_id() {
            return meal_id;
        }

        public int getId() {
            return id;
        }

        public int getFlag() {
            return flag;
        }

        public ComponentModel getComponent() {
            return component;
        }

        public float getAmount() {
            return amount;
        }

        public String getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public class ComponentModel {
            int id, food_type, preparation_time;
            float total_protein, total_carbohydrate, total_fat, serving_unit, total_energy;
            String name, recipe, image;
            ServingType serving_type;
            List<FoodTimeModel> food_time;
            List<CuisineModel> cuisine;
            List<ServingDetailModel> other_serving_detail;
            FoodGroupModel food_group;

            public ComponentModel() {
                serving_type = new ServingType();
                food_group = new FoodGroupModel();
                food_time = new ArrayList<FoodTimeModel>();
                cuisine = new ArrayList<CuisineModel>();
                other_serving_detail = new ArrayList<ServingDetailModel>();
            }

            public List<ServingDetailModel> getOther_serving_detail() {
                return other_serving_detail;
            }

            public int getId() {
                return id;
            }

            public int getFood_type() {
                return food_type;
            }

            public int getPreparation_time() {
                return preparation_time;
            }

            public float getServing_unit() {
                return serving_unit;
            }

            public float getTotal_carbohydrate() {
                return total_carbohydrate;
            }

            public float getTotal_energy() {
                return total_energy;
            }

            public float getTotal_fat() {
                return total_fat;
            }

            public float getTotal_protein() {
                return total_protein;
            }

            public FoodGroupModel getFood_group() {
                return food_group;
            }

            public List<CuisineModel> getCuisine() {
                return cuisine;
            }

            public List<FoodTimeModel> getFood_time() {
                return food_time;
            }

            public ServingType getServing_type() {
                return serving_type;
            }

            public String getImage() {
                return image;
            }

            public String getName() {
                return name;
            }

            public String getRecipe() {
                return recipe;
            }

            public class ServingType {
                int id;
                String serving_type;

                public int getId() {
                    return id;
                }

                public String getServing_type() {
                    return serving_type;
                }
            }

            public class FoodTimeModel {
                int id;
                String food_time;

                public int getId() {
                    return id;
                }

                public String getFood_time() {
                    return food_time;
                }
            }

            public class FoodGroupModel {
                int id;
                String name;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }
            public class ServingDetailModel{
                int id,recipe;
                float total_protein, total_carbohydrate, total_fat, serving_unit, total_energy,saturated_fat,mono_unsaturated_fat,poly_unsaturated_fat,cholesterol,sodium,potassium,calcium,iron,vitamin_c,vitamin_a,fibre;
                ServingType serving_type;

                public float getCholesterol() {
                    return cholesterol;
                }

                public float getMono_unsaturated_fat() {
                    return mono_unsaturated_fat;
                }

                public float getCalcium() {
                    return calcium;
                }

                public float getPoly_unsaturated_fat() {
                    return poly_unsaturated_fat;
                }

                public float getIron() {
                    return iron;
                }

                public float getFibre() {
                    return fibre;
                }

                public float getPotassium() {
                    return potassium;
                }

                public float getSaturated_fat() {
                    return saturated_fat;
                }

                public float getServing_unit() {
                    return serving_unit;
                }

                public float getSodium() {
                    return sodium;
                }

                public float getTotal_carbohydrate() {
                    return total_carbohydrate;
                }

                public float getTotal_energy() {
                    return total_energy;
                }

                public float getTotal_fat() {
                    return total_fat;
                }

                public float getTotal_protein() {
                    return total_protein;
                }

                public float getVitamin_a() {
                    return vitamin_a;
                }

                public float getVitamin_c() {
                    return vitamin_c;
                }

                public int getId() {
                    return id;
                }

                public int getRecipe() {
                    return recipe;
                }

                public ServingType getServing_type() {
                    return serving_type;
                }
            }
        }
    }
}
