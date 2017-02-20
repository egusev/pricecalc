package ru.erfolk.pricecalc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by eugene on 20.02.17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;

    private String password;

    private int requestPerSecond;
}
