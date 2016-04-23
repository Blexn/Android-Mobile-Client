package edu.jhu.bme.cbid.healthassistantsclient.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amal Afroz Alam on 21, April, 2016.
 * Contact me: contact@amal.io
 */
public class Node {

    private String id;
    private String text;
    private String language;
    private String inputType;
    private String physicalExams;
    private List<Node> optionsList;
    private String associatedComplaint;
    private List associatedComplaints;
    private String jobAidFile;
    private String jobAidType;

    private boolean complaint;
    private boolean terminal;
    private boolean hasAssocations;
    private boolean aidAvailable;

    public Node(JSONObject jsonNode) {
        try {
            /**
             * This is the ID generated by the mindmapping software that we use.
             * This value may be used in the future, but currently it is not implemented anywhere.
             */
            this.id = jsonNode.getString("id");

            /**
             * All nodes have text, or a title associated with them. This makes it easier to identify them.
             */
            this.text = jsonNode.getString("text");

            /**
             * Only nodes which have further branches will have an "options" array.
             * Otherwise, this means that the complaint/question pathway is complete, and we've reached a terminal node.
             */
            JSONArray optionsArray = jsonNode.optJSONArray("options");
            if (optionsArray == null) {
                this.terminal = true;
            } else {
                this.terminal = false;
                this.optionsList = createOptions(optionsArray);
            }

            /**
             * Almost all nodes have a language attribute which becomes the displayed text to the user.
             */
            this.language = jsonNode.optString("language");

            /**
             * There are various input types available to each node.
             * These include text, number, date, range, etc.
             * The input-type is recorded and retrieved when the node is a question.
             */
            this.inputType = jsonNode.optString("input-type");

            /**
             * Physical exams are only present on complaints.
             * Complaints are display at the very beginning of the workflow to be selected from.
             */
            this.physicalExams = jsonNode.optString("perform-physical-exam");
            if (!physicalExams.isEmpty()) {
                this.complaint = true;
            } else {
                this.complaint = false;
            }

            /**
             * Job aids are presented with questions, to assist in answering a question.
             * Aids can be video, audio, or images.
             */
            this.jobAidFile = jsonNode.optString("job-aid-file");
            if (!jobAidFile.isEmpty()) {
                this.jobAidType = jsonNode.optString("job-aid-type");
                this.aidAvailable = true;
            } else {
                this.aidAvailable = false;
            }

            /**
             * Some terminal nodes may have associated complaints.
             * We account for this by storing the name of the associated complaint, and search for that object in the knowledge DB.
             */
            this.associatedComplaint = jsonNode.optString("associated-complaint");
            if (!associatedComplaint.isEmpty()) {
                this.hasAssocations = false;
            } else {
                this.hasAssocations = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Node> createOptions(JSONArray jsonArray) {
        List<Node> createdOptions = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject current = jsonArray.getJSONObject(i);
                createdOptions.add(i, new Node(current));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return createdOptions;
    }

    public String type() {
        return inputType;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isComplaint() {
        return complaint;
    }

    public String language() {
        return language;
    }

    public String text() {
        return text;
    }

    public String id() {
        return id;
    }

    public boolean hasAssociations() {
        return hasAssocations;
    }


}
