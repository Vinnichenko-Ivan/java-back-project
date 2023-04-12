package com.hits.friends.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="friendship",  uniqueConstraints={
        @UniqueConstraint(columnNames={"main_user", "target_user"}),
})
public class Friendship extends Relationship {


}
