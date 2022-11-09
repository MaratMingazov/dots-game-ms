package com.dotsgamems.mongo;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class MongoMove {

    int x;
    int y;
    double probability;
}
