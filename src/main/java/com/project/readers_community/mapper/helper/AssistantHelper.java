package com.project.readers_community.mapper.helper;

import com.project.readers_community.model.common.MessageResponse;

import java.time.LocalDateTime;
import java.util.*;

public class AssistantHelper {
   public static Map<Locale, String> trimMapValues(Map<Locale, String> originalMap) {
      if (originalMap == null) {
         return null;
      }
      Map<Locale, String> trimmedMap = new HashMap<>();
      for (Map.Entry<Locale, String> entry : originalMap.entrySet()) {
         trimmedMap.put(entry.getKey(), entry.getValue().trim());
      }
      return trimmedMap;
   }

   public static Map<String, String> trimMapStringValues(Map<String, String> originalMap) {
      if (originalMap == null) {
         return null;
      }
      Map<String, String> trimmedMap = new HashMap<>();
      for (Map.Entry<String, String> entry : originalMap.entrySet()) {
         trimmedMap.put(entry.getKey().trim(), entry.getValue().trim());
      }
      return trimmedMap;
   }

   public static Map<String, Double> trimKeyMapValues(Map<String, Double> originalMap) {
      if (originalMap == null) {
         return null;
      }
      Map<String, Double> trimmedKeyMap = new HashMap<>();
      for (Map.Entry<String, Double> entry : originalMap.entrySet()) {
         trimmedKeyMap.put(entry.getKey().trim(), entry.getValue());
      }
      return trimmedKeyMap;
   }

   public static List<String> trimListValues(List<String> originalList) {
      if (originalList == null) {
         return new ArrayList<>();
      }
      List<String> trimmedList = new ArrayList<>();
      for (String element : originalList) {
         trimmedList.add(element.trim());
      }
      return trimmedList;
   }

   public static List<String> trimListAndUnique(List<String> originalList) {
      if (originalList == null) {
         return new ArrayList<>();
      }

      // Use a Set to ensure uniqueness
      Set<String> uniqueTrimmedSet = new HashSet<>();

      // Trim each element and add to the set to ensure uniqueness
      for (String element : originalList) {
         if (element != null) {
            uniqueTrimmedSet.add(element.trim());
         }
      }

      // Convert the set back to a list
      return new ArrayList<>(uniqueTrimmedSet);
   }

   public static LocalDateTime getCurrentDate() {
      return LocalDateTime.now();
   }

   public static String trimString(String originalString) {
      return originalString == null ? null : originalString.trim();
   }

   public static MessageResponse toMessageResponse(String msg){
      return MessageResponse.builder()
              .message(msg)
              .build();
   }
}
