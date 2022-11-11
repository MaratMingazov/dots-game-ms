package com.dotsgamems.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MongoMove {

    int x;
    int y;
    int probability;
}
