package itstep.learning.models.form;

public class ShopCategoryFormModel {
    private String name;
    private String description;
    private String savedFilename;
    private String slug;

    public ShopCategoryFormModel() {
    }

    public String getSlug() {
        return slug;
    }

    public ShopCategoryFormModel setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getName() {
        return name;
    }

    public ShopCategoryFormModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ShopCategoryFormModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSavedFilename() {
        return savedFilename;
    }

    public ShopCategoryFormModel setSavedFilename(String savedFilename) {
        this.savedFilename = savedFilename;
        return this;
    }
}
