package org.example.expert.domain.log.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLog is a Querydsl query type for Log
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLog extends EntityPathBase<Log> {

    private static final long serialVersionUID = 2144785399L;

    public static final QLog log = new QLog("log");

    public final org.example.expert.domain.common.entity.QTimestamped _super = new org.example.expert.domain.common.entity.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> managerId = createNumber("managerId", Long.class);

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<org.example.expert.domain.log.enums.SaveManagerStatus> status = createEnum("status", org.example.expert.domain.log.enums.SaveManagerStatus.class);

    public final NumberPath<Long> todoId = createNumber("todoId", Long.class);

    public QLog(String variable) {
        super(Log.class, forVariable(variable));
    }

    public QLog(Path<? extends Log> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLog(PathMetadata metadata) {
        super(Log.class, metadata);
    }

}

