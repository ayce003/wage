package com.ycjd.payslip.controller.worker;

import com.ycjd.payslip.pojo.Pager;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.worker.Post;
import com.ycjd.payslip.pojo.worker.Worker;
import com.ycjd.payslip.service.worker.PostService;
import com.ycjd.payslip.service.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private WorkerService workerService;

    @PostMapping("/savePost")
    public ResponseJson savePost(
            @RequestBody Post post){
       postService.savePost(post);
        return new ResponseJson();
    }

    @GetMapping("/update/findPostById/{id}")
    public ResponseJson findPostById(
            @PathVariable String id){
        Post post=postService.findPostById(id);
        return new ResponseJson(post);
    }

    @PostMapping("/update/updatePost")
    public ResponseJson updatePost(
            @RequestBody Post post){
        postService.updatePost(post);
        return new ResponseJson();
    }

    @GetMapping("/look/lookPostById/{id}")
    public ResponseJson lookPostById(
            @PathVariable String id){
        Post post=postService.findPostById(id);
        return new ResponseJson(post);
    }

    @PostMapping("/findPostsByCondition")
    public ResponseJson findPostsByCondition(
            @Validated
            @RequestBody Post post){
        Pager pager = post.getPager().setLike("postName");
        post.setPager(pager);
        List<Post> data=postService.findPostListByCondition(post);
        long count=postService.findPostCountByCondition(post);
        return new ResponseJson(data,count);
    }
    @PostMapping("/findOnePostByCondition")
    public ResponseJson findOnePostByCondition(
            @RequestBody Post post){
        Post one=postService.findOnePostByCondition(post);
        return new ResponseJson(one);
    }
    @GetMapping("/deletePost/{id}")
    public ResponseJson deletePost(
            @PathVariable String id){
        postService.deletePost(id);
        Worker worker = new Worker();
        worker.setPostId(id);
        List<Worker> workerList = workerService.findWorkerListByCondition(worker);
        if(workerList!=null&&workerList.size()>0){
            workerList.forEach(w ->{
                w.setPostId("");
                workerService.updateWorker(w);
            } );

        }
        return new ResponseJson();
    }


    //查找
    @PostMapping("/findPostListByCondition")
    public ResponseJson findPostListByCondition(
            @RequestBody Post post){
        List<Post> data=postService.findPostListByCondition(post);
        Long size = Long.valueOf(data.size());
        return new ResponseJson(data,size);
    }



}
