package com.web.mapper;

import com.web.dto.response.UserDto;
import com.web.dto.response.WorkProcessResponse;
import com.web.entity.User;
import com.web.entity.WorkProcess;
import com.web.entity.WorkProcessStudent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkProcessMapper {

    @Autowired
    private ModelMapper mapper;

    public WorkProcessResponse entityToResponse(WorkProcess workProcess){
        WorkProcessResponse dto = mapper.map(workProcess, WorkProcessResponse.class);
        return dto;
    }

    public WorkProcessResponse.WorkProcessStudentResponse entityToResponse(WorkProcessStudent workProcessStudent){
        WorkProcessResponse.WorkProcessStudentResponse dto = mapper.map(workProcessStudent, WorkProcessResponse.WorkProcessStudentResponse.class);
        return dto;
    }

}
