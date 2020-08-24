package com.robert.src;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Request {

    private String pageSourceApi;

    private ArrayList<Booking> morningList = new ArrayList<>();

    private ArrayList<Booking> afternoonList = new ArrayList<>();

    private ArrayList<Booking> eveningList = new ArrayList<>();

    private Booking specificBooking;

    public void getJson() throws IOException {

        morningList.clear();
        afternoonList.clear();
        eveningList.clear();

        InputStream inputStream = new ByteArrayInputStream(pageSourceApi.getBytes(StandardCharsets.UTF_8));

        InputStreamReader streamReader = new InputStreamReader(inputStream);

        JsonReader jsonReader = new JsonReader(streamReader);
        jsonReader.setLenient(true);

        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray jsonArrayMorning = jsonObject.get("MorningList").getAsJsonArray();

        JsonArray jsonArrayAfternoon = jsonObject.get("AfternoonList").getAsJsonArray();

        JsonArray jsonArrayEvening = jsonObject.get("EveningList").getAsJsonArray();

        int arrayIndexMorning = 0;

        int arrayIndexAfternoon = 0;

        int arrayIndexEvening = 0;

        for(JsonElement element : jsonArrayMorning) {

            JsonObject listJsonObject = element.getAsJsonObject();

            String startAt = listJsonObject.get("StartAtDisplay").getAsString();
            String endAt = listJsonObject.get("EndAtDisplay").getAsString();
            int spotsAvailable = listJsonObject.get("SpotsAvailable").getAsInt();


            Booking booking = new Booking(startAt, endAt, spotsAvailable, arrayIndexMorning);

            this.morningList.add(booking);

            arrayIndexMorning++;

        }

        for(JsonElement element : jsonArrayAfternoon) {

            JsonObject listJsonObject = element.getAsJsonObject();

            String startAt = listJsonObject.get("StartAtDisplay").getAsString();
            String endAt = listJsonObject.get("EndAtDisplay").getAsString();
            int spotsAvailable = listJsonObject.get("SpotsAvailable").getAsInt();

            Booking booking = new Booking(startAt, endAt, spotsAvailable, arrayIndexAfternoon);

            this.afternoonList.add(booking);

            arrayIndexAfternoon++;

        }

        for(JsonElement element : jsonArrayEvening) {

            JsonObject listJsonObject = element.getAsJsonObject();

            String startAt = listJsonObject.get("StartAtDisplay").getAsString();
            String endAt = listJsonObject.get("EndAtDisplay").getAsString();
            int spotsAvailable = listJsonObject.get("SpotsAvailable").getAsInt();

            Booking booking = new Booking(startAt, endAt, spotsAvailable, arrayIndexEvening);

            this.eveningList.add(booking);

            arrayIndexEvening++;

        }

        streamReader.close();

        jsonReader.close();

        inputStream.close();

    }

    public void getSpecificJsonObject(int arrayIndex, String section) throws IOException {

        // STRING SECTION MUST BE EITHER:
        // Morning
        // Afternoon
        // Evening

        InputStream inputStream = new ByteArrayInputStream(pageSourceApi.getBytes(StandardCharsets.UTF_8));

        InputStreamReader streamReader = new InputStreamReader(inputStream);

        JsonReader jsonReader = new JsonReader(streamReader);
        jsonReader.setLenient(true);

        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        switch (section) {
            case "Morning" -> {

                JsonArray jsonMorningArray = jsonObject.get("MorningList").getAsJsonArray();

                JsonElement jsonElement1 = jsonMorningArray.get(arrayIndex);

                JsonObject jsonArrayObj = jsonElement1.getAsJsonObject();

                String startAt = jsonArrayObj.get("StartAtDisplay").getAsString();
                String endAt = jsonArrayObj.get("EndAtDisplay").getAsString();
                int spotsAvailable = jsonArrayObj.get("SpotsAvailable").getAsInt();

                Booking specificBooking = new Booking(startAt, endAt, spotsAvailable, arrayIndex);

                this.specificBooking = specificBooking;

                jsonReader.close();
                streamReader.close();
                inputStream.close();

            }
            case "Afternoon" -> {

                JsonArray jsonAfternoonArray = jsonObject.get("AfternoonList").getAsJsonArray();

                JsonElement jsonElement1 = jsonAfternoonArray.get(arrayIndex);

                JsonObject jsonArrayObj = jsonElement1.getAsJsonObject();

                String startAt = jsonArrayObj.get("StartAtDisplay").getAsString();
                String endAt = jsonArrayObj.get("EndAtDisplay").getAsString();
                int spotsAvailable = jsonArrayObj.get("SpotsAvailable").getAsInt();

                Booking specificBooking = new Booking(startAt, endAt, spotsAvailable, arrayIndex);

                this.specificBooking = specificBooking;

                jsonReader.close();
                streamReader.close();
                inputStream.close();

            }
            case "Evening" -> {

                JsonArray jsonEveningArray = jsonObject.get("EveningList").getAsJsonArray();

                JsonElement jsonElement1 = jsonEveningArray.get(arrayIndex);

                JsonObject jsonArrayObj = jsonElement1.getAsJsonObject();

                String startAt = jsonArrayObj.get("StartAtDisplay").getAsString();
                String endAt = jsonArrayObj.get("EndAtDisplay").getAsString();
                int spotsAvailable = jsonArrayObj.get("SpotsAvailable").getAsInt();

                Booking specificBooking = new Booking(startAt, endAt, spotsAvailable, arrayIndex);

                this.specificBooking = specificBooking;

                jsonReader.close();
                streamReader.close();
                inputStream.close();

            }
        }

    }

    public ArrayList<Booking> getMorningList() { return morningList; }

    public ArrayList<Booking> getAfternoonList() { return afternoonList; }

    public ArrayList<Booking> getEveningList() { return eveningList; }

    public Booking getSpecificBooking() {return specificBooking;}

    public void setPageSourceApi(String pageSourceApi) {

        this.pageSourceApi = pageSourceApi;

    }
}
