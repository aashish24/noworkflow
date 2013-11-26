package br.uff.ic.sel.noworkflow.view;

import br.uff.ic.sel.noworkflow.controller.SQLiteReader;
import br.uff.ic.sel.noworkflow.model.Flow;
import br.uff.ic.sel.noworkflow.model.FunctionCall;
import edu.uci.ics.jung.algorithms.filters.Filter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author leomurta
 */
public class GraphFrame extends javax.swing.JFrame {

    /**
     * Creates new form GraphFrame
     */
    public GraphFrame(DirectedGraph<FunctionCall, Flow> graph) {
        initComponents();
        initGraphComponent(graph);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Young Researchers in Software Engineering");

        setSize(new java.awt.Dimension(816, 638));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initGraphComponent(DirectedGraph<FunctionCall, Flow> graph) {
        // Filtering vertices with less than 2 indications
        Filter<FunctionCall, Flow> filter = new VertexPredicateFilter<>(new Predicate<FunctionCall>() {
            @Override
            public boolean evaluate(FunctionCall researcher) {
                return true;  //researcher.getNominationsCount() > 1;
            }
        });
        graph = (DirectedGraph<FunctionCall, Flow>) filter.transform(graph);

        // Choosing layout
//        Layout<Researcher, Indication> layout = new CircleLayout<Researcher, Indication>(graph);
//        Layout<Researcher, Indication> layout = new FRLayout2<Researcher, Indication>(graph);
//        Layout<Researcher, Indication> layout = new ISOMLayout<Researcher, Indication>(graph);
//        Layout<Researcher, Indication> layout = new KKLayout<>(graph);
        Layout<FunctionCall, Flow> layout = new SpringLayout2<>(graph);
//        layout.setSize(new Dimension(2000, 2000));

        VisualizationViewer<FunctionCall, Flow> view = new VisualizationViewer<>(layout);

        // Adding interation via mouse
        DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
        view.setGraphMouse(mouse);
        view.addKeyListener(mouse.getModeKeyListener());


        // Labelling vertices
//        view.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Researcher>());

        // Painting vertices
        Transformer vertexPainter = new Transformer<FunctionCall, Paint>() {
            @Override
            public Paint transform(FunctionCall researcher) {
                int tone = Math.round(255);  // * ( 1 - researcher.getNominationsCount() / (float) Function.getMaxNominationsCount() ));
                return new Color(tone, tone, tone);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
        view.getRenderContext().setVertexDrawPaintTransformer(vertexPainter);

        // Painting edges
        Transformer edgePainter = new Transformer<Flow, Paint>() {
            @Override
            public Paint transform(Flow indication) {
                int tone = Math.round(255); // * ( 1 - indication.getSource().getNominationsCount() / (float) Function.getMaxNominationsCount() ));
                return new Color(tone, tone, tone);
            }
        };
        view.getRenderContext().setEdgeDrawPaintTransformer(edgePainter);
        view.getRenderContext().setArrowDrawPaintTransformer(edgePainter);
        view.getRenderContext().setArrowFillPaintTransformer(edgePainter);

        view.setBackground(Color.white);
        this.getContentPane().add(view, BorderLayout.CENTER);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional)">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        try {
            //<editor-fold defaultstate="collapsed" desc="Load graph file">
            SQLiteReader tsvReader = new SQLiteReader("graph.tsv", 1);
            final DirectedGraph<FunctionCall, Flow> graph = new DirectedSparseGraph<FunctionCall, Flow>();
            for (FunctionCall researcher : tsvReader.getFunctionCalls()) {
                graph.addVertex(researcher);
            }
            for (Flow indication : tsvReader.getFlows()) {
                graph.addEdge(indication, indication.getSource(), indication.getTarget());
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Create and display the form">
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new GraphFrame(graph).setVisible(true);
                }
            });
            //</editor-fold>
        }
        catch (Exception ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
