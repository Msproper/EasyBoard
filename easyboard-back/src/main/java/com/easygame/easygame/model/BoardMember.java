//package com.easygame.easygame.model;
//
//import com.easygame.easygame.enums.BoardRole;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "board_members")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class BoardMember {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "board_id", nullable = false)
//    private BoardModel board;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserModel user;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private BoardRole role;
//
//    public BoardMember(BoardModel board, UserModel user, BoardRole role) {
//        this.board = board;
//        this.user = user;
//        this.role = role;
//    }
//}