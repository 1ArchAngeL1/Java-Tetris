import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class JBrainTetris extends JTetris {

    private Brain brain = new DefaultBrain();
    private boolean needCalculate;
    private Brain.Move move;
    private JLabel brainText = new JLabel("Brain:");
    private JCheckBox brainMode = new JCheckBox("Brain active");
    private JCheckBox animateMode = new JCheckBox("Animate fall");
    private JLabel adversaryStatus  = new JLabel("OK-");
    private JSlider  adversary = new JSlider(0, 100, 0);

    JBrainTetris(int pixels) {
        super(pixels);
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel)super.createControlPanel();
        JPanel tempPannel = new JPanel();
        adversary.setPreferredSize(new Dimension(100,15));
        tempPannel.add(new JLabel("Advrsary:"));
        tempPannel.add(adversary);
        panel.add(brainText);
        panel.add(brainMode);
        animateMode.setSelected(true);
        panel.add(animateMode);
        panel.add(tempPannel);
        panel.add(adversaryStatus);
        return panel;
    }


    @Override
    public void addNewPiece() {
        needCalculate = true;
        super.addNewPiece();
    }

    @Override
    public Piece pickNextPiece() {
        if (random.nextInt(99) < adversary.getValue()) {
            Piece next = super.pickNextPiece();
            double currScore = 0;
            adversaryStatus.setText("*OK*");
            for (int i = 0;i < pieces.length;++i) {
                board.undo();
                Brain.Move nextMove = brain.bestMove(board, pieces[i], board.getHeight(), null);
                if (nextMove != null && nextMove.score > currScore) {
                    next = pieces[i];
                    currScore = nextMove.score;
                }
            }
            return next;
        }
        adversaryStatus.setText("OK");
        return super.pickNextPiece();
    }

    @Override
    public void tick(int verb) {
        if (brainMode.isSelected()) {
            if(verb != DOWN)return;
            if (needCalculate) {
                board.undo();
                move = brain.bestMove(board, currentPiece, board.getHeight(), move);
            }
            if(move != null){
                if(!move.piece.equals(currentPiece))super.tick(ROTATE);
                if(move.x < currentX)super.tick(LEFT);
                if(move.x > currentX)super.tick(RIGHT);
                if(!animateMode.isSelected() && move.x == currentX && currentY > move.y)super.tick(DROP);
                if(needCalculate)needCalculate = false;
            }
        }
        super.tick(verb);
    }

    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }
}





