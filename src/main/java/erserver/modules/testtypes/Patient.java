package erserver.modules.testtypes;

import erserver.modules.dependencies.Priority;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient {

   private int transportId;
   private String name;
   private LocalDate birthDate;
   private Priority priority;
   private String condition;

   public int getTransportId() {
      return transportId;
   }

   public void setTransportId(int transportId) {
      this.transportId = transportId;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public ChildClassification getChildClassification() {
      return ChildClassification.calculate(birthDate, getSystemCurrentDate());
   }

   protected LocalDate getSystemCurrentDate(){
      return LocalDate.now();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Priority getPriority() {
      return priority;
   }

   public void setPriority(Priority priority) {
      this.priority = priority;
   }

   public String getCondition() {
      return condition;
   }

   public void setCondition(String condition) {
      this.condition = condition;
   }
}
