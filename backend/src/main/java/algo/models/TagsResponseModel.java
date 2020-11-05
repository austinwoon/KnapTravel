package algo.models;

import java.util.List;

public class TagsResponseModel {
    List<String> tags;
    public TagsResponseModel(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
