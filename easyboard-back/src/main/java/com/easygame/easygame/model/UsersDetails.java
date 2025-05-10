package com.easygame.easygame.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "UsersDetails")
public class UsersDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_details_id_seq")
    @SequenceGenerator(name = "user_details_id_seq", sequenceName = "user_details_id_seq", allocationSize = 1)
    private Long id;

    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String createAt;
    @Column
    private String status;
    @Column
    private String photoPath;

    @Override
    public String toString() {
        return "UsersDetails{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", createAt='" + createAt + '\'' +
                ", status='" + status + '\'' +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }
}
