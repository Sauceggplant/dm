package sut.edu.zyp.dormitory.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sut.edu.zyp.dormitory.manage.entity.LiveEntity;

/**
 * 入住信息存储服务
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@RepositoryRestResource(collectionResourceRel = "live", path = "live")
public interface LiveRepository extends JpaRepository<LiveEntity, String> {
}
