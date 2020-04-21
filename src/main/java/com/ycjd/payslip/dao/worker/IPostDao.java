package com.ycjd.payslip.dao.worker;

import com.ycjd.payslip.pojo.worker.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IPostDao {
    List<Post> findPostListByCondition(Post post);

    long findPostCountByCondition(Post post);

    Post findOnePostByCondition(Post post);

    Post findPostById(@Param("id") String id);

    void savePost(Post post);

    void updatePost(Post post);

    void deletePost(@Param("id") String id);

    void deletePostByCondition(Post post);

    void batchSavePost(List<Post> posts);
}
