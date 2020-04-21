package com.ycjd.payslip.pojo;

import com.twitter.snowflake.sequence.IdSequence;
import com.twitter.snowflake.support.SecondsIdSequenceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SequenceId {
    private IdSequence idSequence;
    public SequenceId() {
        idSequence=new SecondsIdSequenceFactory().create(9966);
    }
    public String nextId(){
        return idSequence.nextId() + "";
    }

    public String parse(String id){
        return idSequence.parse(Long.valueOf(id));
    }

}
