package com.ycjd.payslip.service.worker;

import com.ycjd.payslip.dao.worker.IPostDao;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.worker.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private IPostDao postDao;
    @Autowired
    private SequenceId sequenceId;

    @Transactional(readOnly = true)
    public Post findPostById(String id) {
        return postDao.findPostById(id);
    }
    @Transactional
    public void savePost(Post post) {
        post.setId(sequenceId.nextId());
        postDao.savePost(post);
    }
    @Transactional(readOnly = true)
    public List<Post> findPostListByCondition(Post post) {
        return postDao.findPostListByCondition(post);
    }
    @Transactional(readOnly = true)
    public Post findOnePostByCondition(Post post) {
        return postDao.findOnePostByCondition(post);
    }
    @Transactional(readOnly = true)
    public long findPostCountByCondition(Post post) {
        return postDao.findPostCountByCondition(post);
    }
    @Transactional
    public void updatePost(Post post) {
        postDao.updatePost(post);
    }
    @Transactional
    public void deletePost(String id) {
        postDao.deletePost(id);
    }
    @Transactional
    public void deletePostByCondition(Post post) {
        postDao.deletePostByCondition(post);
    }
    @Transactional
    public void batchSavePost(List<Post> posts){
        posts.forEach(post -> post.setId(sequenceId.nextId()));
        postDao.batchSavePost(posts);
    }

}
