package com.bypark.userstory.todolist.services;

import com.bypark.userstory.dto.ToDoModifyRequest;
import com.bypark.userstory.dto.ToDoRegisterRequest;
import com.bypark.userstory.dto.ToDoResponse;
import com.bypark.userstory.todolist.model.ToDoEntity;
import com.bypark.userstory.todolist.repository.ToDoListRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ToDoService {

    private final ToDoListRepository toDoListRepository;

    public ToDoService(ToDoListRepository toDoListRepository){
        this.toDoListRepository = toDoListRepository;
    }

    // API2
    // todo를 신규 생성
    public ToDoEntity add(ToDoRegisterRequest request){
        ToDoEntity toDoEntity = new ToDoEntity();
        toDoEntity.setRegDate(request.getRegDate());
        toDoEntity.setTask(request.getTask());
        toDoEntity.setDes(request.getDes());

        toDoEntity.setPriority("B");
        toDoEntity.setStatus("진행중");

        int tmp = 0;
        for(ToDoEntity toDoEntity1 : toDoListRepository.listAll()){
            if(toDoEntity1.getPriority().equals("B")){
                if(tmp <= toDoEntity1.getOrder()){
                    tmp = toDoEntity1.getOrder() + 1;
                }
            }
        }
        toDoEntity.setOrder(tmp);

        return this.toDoListRepository.save(toDoEntity);
    }

    // API1
    // 특정날짜의 모든 todo들을 우선순위로 정렬하여 반환
    public List<ToDoResponse> findAll(LocalDate date){
        List<ToDoResponse> list = new ArrayList<>();
        List<ToDoEntity> listS = new ArrayList<>();
        List<ToDoEntity> listA = new ArrayList<>();
        List<ToDoEntity> listB = new ArrayList<>();
        List<ToDoEntity> listC = new ArrayList<>();
        List<ToDoEntity> listD = new ArrayList<>();

        for(ToDoEntity toDoEntity : toDoListRepository.listAll()){
            if(toDoEntity.getRegDate().equals(date)){
                if(toDoEntity.getPriority().equals("S")){
                    listS.add(toDoEntity);
                } else if(toDoEntity.getPriority().equals("A")){
                    listA.add(toDoEntity);
                } else if(toDoEntity.getPriority().equals("B")){
                    listB.add(toDoEntity);
                } else if(toDoEntity.getPriority().equals("C")){
                    listC.add(toDoEntity);
                } else if(toDoEntity.getPriority().equals("D")){
                    listD.add(toDoEntity);
                }
            }
        }
        Collections.sort(listS);
        Collections.sort(listA);
        Collections.sort(listB);
        Collections.sort(listC);
        Collections.sort(listD);

        for(ToDoEntity tmp : listS){
            list.add(new ToDoResponse(tmp));
        }
        for(ToDoEntity tmp : listA){
            list.add(new ToDoResponse(tmp));
        }
        for(ToDoEntity tmp : listB){
            list.add(new ToDoResponse(tmp));
        }
        for(ToDoEntity tmp : listC){
            list.add(new ToDoResponse(tmp));
        }
        for(ToDoEntity tmp : listD){
            list.add(new ToDoResponse(tmp));
        }
        return list;
    }

    // 특정 todo가 해당하는 우선순위(글자: priority)를 갖는 todo들을 전체 갯수 반환
    public int countOrderMax(ToDoEntity e){
        int count = 0;
        for(ToDoEntity tmp : toDoListRepository.listAll()){
            if(tmp.getPriority().equals(e.getPriority())){
                count++;
            }
        }
        return count;
    }

    // 두 todo의 우선순위(숫자: order) 맞바꿈
    public void swapOrder(ToDoEntity e1, ToDoEntity e2){
        int tmpOrder = e1.getOrder();
        e1.setOrder(e2.getOrder());
        e2.setOrder(tmpOrder);
    }

    // 특정 우선순위(글자: priority)인 todo들 중에서 마지막 todo의 우선순위(숫자: order)을 destination으로 이동
    public void shiftOrder(int destination, String prior){
        List<ToDoEntity> lst = new ArrayList<>();
        for(ToDoEntity tmp : toDoListRepository.listAll()){
            if(tmp.getPriority().equals(prior)){
                lst.add(tmp);
            }
        }
        Collections.sort(lst);
        for(int i = lst.size()-1; i > destination; i--){
            swapOrder(lst.get(i), lst.get(i-1));
            Collections.sort(lst);
        }
    }

    // 누락된 우선순위(숫자) 정리
    public void replaceOrder(String prior){
        List<ToDoEntity> lst = new ArrayList<>();
        for(ToDoEntity tmp : toDoListRepository.listAll()){
            if(tmp.getPriority().equals(prior)){
                lst.add(tmp);
            }
        }
        Collections.sort(lst);
        for(int i = 0; i < lst.size(); i++){
            lst.get(i).setOrder(i);
        }
    }

    // id 값을 통해 todo 존재 여부확인
    public boolean searchById(Long id){
        return this.toDoListRepository.findById(id).isEmpty();
    }

    // API 3
    // 특정 todo를 변경함
    public ToDoEntity modify(ToDoModifyRequest request){
        ToDoEntity toDoEntity = this.toDoListRepository.findById(request.getIndex()).get();

        if(request.getTask() != null){
            toDoEntity.setTask(request.getTask());
        }
        if(request.getDes() != null){
            toDoEntity.setDes(request.getDes());
        }
        if(request.getStatus() != null){
            if(request.getStatus().equals("진행중") || request.getStatus().equals("완료") || request.getStatus().equals("취소")){
                toDoEntity.setStatus(request.getStatus());
            }
        }
        String originPrior = toDoEntity.getPriority();
        if(request.getPriority() != null){
            if(request.getPriority().equals("S") || request.getPriority().equals("A") || request.getPriority().equals("B") ||
                    request.getPriority().equals("C") || request.getPriority().equals("D")){
                toDoEntity.setPriority(request.getPriority());
            }
        }

        System.out.println(request.getOrder());
        if(request.getOrder() >= 0 && request.getOrder() <= countOrderMax(toDoEntity)+1){
            if(request.getOrder() == countOrderMax(toDoEntity)+1){
                toDoEntity.setOrder(request.getOrder());
            } else{
                // 수정하려는 todo의 order를 가장 마지막 값을 할당한 후
                toDoEntity.setOrder(countOrderMax(toDoEntity)+1);
                // 지정한 order의 위치로 한칸 씩 이동하여 값을 맞바꿈
                shiftOrder(request.getOrder(), request.getPriority());
                // 누락된 order가 없도록 설정 (기존 priority와 바꾸려는 priority 모두)
                replaceOrder(request.getPriority());
                replaceOrder(originPrior);
            }
        }
        return toDoEntity;
    }

    public void delete(Long id){
        ToDoEntity toDoEntity = this.toDoListRepository.findById(id).get();
        String tmp = toDoEntity.getPriority();
        this.toDoListRepository.deleteById(id);
        replaceOrder(tmp);
    }

}
