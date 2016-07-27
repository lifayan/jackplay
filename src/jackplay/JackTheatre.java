package jackplay;

import jackplay.play.*;
import jackplay.play.performers.LeadPerformer;
import jackplay.web.BoxOffice;

import java.lang.instrument.*;

public class JackTheatre implements Theatre {
    static JackTheatre jackTheatre;

    Options options;
    Instrumentation inst;
    Composer composer;
    ProgramManager pm;
    LeadPerformer leader;
    BoxOffice boxOffice;

    public JackTheatre(Options options, Instrumentation inst, Composer composer, ProgramManager pm, LeadPerformer leader, BoxOffice boxOffice) {
        this.options = options;
        this.inst = inst;
        this.composer = composer;
        this.pm = pm;
        this.leader = leader;
        this.boxOffice = boxOffice;
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        Logger.info("running JackPlay with arguments:" + (agentArgs == null ? "(no args, resort to default)" : agentArgs));
        Logger.debug("Instrumentation.isRetransformClassesSupported():" + inst.isRetransformClassesSupported());
        Logger.debug("Instrumentation.isRedefineClassesSupported():" + inst.isRedefineClassesSupported());

        Options options = Options.optionsMergedWithDefaults(agentArgs);
        Logger.info("open your browser and point to http://localhost:" + options.port());

        Logger.init(options);
        PlayKeeper.init(options);

        Composer composer = new Composer();
        ProgramManager pm = new ProgramManager();
        LeadPerformer leader = new LeadPerformer();
        BoxOffice boxOffice = new BoxOffice();

        jackTheatre = new JackTheatre(options, inst, composer, pm, leader, boxOffice);

        jackTheatre.start();
    }

    @Override
    public Options getOptions() {
        return this.options;
    }

    @Override
    public Instrumentation getInstrumentation() {
        return this.inst;
    }

    public Composer getComposer() {
        return this.composer;
    }

    public ProgramManager getProgramManager() {
        return this.pm;
    }

    public LeadPerformer getLeadPerformer() {
        return this.leader;
    }

    public void start() {
        composer.init(this);
        pm.init(this);
        leader.init(this);
        boxOffice.init(this);
        InformationCenter.init(pm);

        this.boxOffice.start();
    }
}