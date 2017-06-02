package gameLogic.game;

import gameLogic.common.CommonFunctions;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.event_system.messages.PlatformState;
import gameLogic.gameComponents.Platform;
import org.jetbrains.annotations.Nullable;
import sample.websocket.Message;
import sample.websocket.WSDict;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Game {
    //todo wire event bus

    private GameWorld world;
    private long lastUpdateTime;
    private long lastTransmitTime;
    private int id;
    private boolean running;
    private AtomicInteger packageCounter;
    private LinkedList<Message<GameWorldState>> messageList;

    public Game(int id) {
        this.id = id;
        running = true;

        packageCounter = new AtomicInteger();
        messageList = new LinkedList<>();
        init();
    }

    public void init() {
        initWorld();
        setListeners();

        final long timestamp = System.currentTimeMillis();
        lastUpdateTime = timestamp;
        lastTransmitTime = timestamp;
    }

    public void saveMessage(Message<GameWorldState> message) {
        messageList.add(message);
        cleanMessageList();
    }

    public List<Message<GameWorldState>> getMessageList(Message<GameWorldState> message) {
        return IntStream.range(0, getUserNum()).boxed()
                .map(i -> {
                    final Message<GameWorldState> localMessage = new Message<>(
                            message.getType(),
                            message.getData().getDiscreteRotation(-i, getUserNum())
                    );
                    localMessage.setId(message.getId());
                    localMessage.setTimestamp(message.getTimestamp());

                    localMessage.getData().getPlatformsState().forEach(platformState -> platformState.setActive(false));
                    localMessage.getData().getPlatformsState().get(0).setActive(true);

                    return localMessage;
                })
                .collect(Collectors.toList());
    }

    public Message<GameWorldState> getWorldStateMessage() {
        final GameWorldState state = world.getState();
        final Message<GameWorldState> message = new Message<>(WSDict.WORLD_STATE, state);
        message.setId(getPackageId());
        message.setTimestamp(System.currentTimeMillis());

        return message;
    }

    public synchronized void alterPast(int playerId, PlatformState platformState) {
        final Integer messageIndex = getSnapshotIndex(platformState.getLastMessageId());
        if (messageIndex != null) {
            revertGameWorld(playerId, platformState, messageList.get(messageIndex).getData(), world);
        }

        resimulateGameWorld(
                world,
                messageList,
                messageIndex == null ? messageList.size() : messageIndex
        );
    }

    public int getId() {
        return id;
    }

    public int getPackageId() {
        return packageCounter.getAndIncrement();
    }

    public boolean isRunning() {
        return running;
    }

    public void pause() {
        running = false;
    }

    public void play() {
        running = true;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void resetLastUpdateTime() {
        lastUpdateTime = System.currentTimeMillis();
    }

    public synchronized long getLastTransmitTime() {
        return lastTransmitTime;
    }

    public synchronized void resetLastTransmitTime() {
        lastTransmitTime = System.currentTimeMillis();
    }

    public int getUserNum() {
        return world.getUserNum();
    }

    public Platform getPlatformByIndex(int index) {
        return CommonFunctions.getByCircularIndex(world.getPlatforms(), index);
    }

    public void makeIteration(double time) {
        world.makeIteration(time);
    }

    private void initWorld() {
        final double sectorHeight = GameConfig.FIELD_SIZE * GameConfig.FILL_FACTOR / 2;
        final double ballRadius = GameConfig.BALL_RELATIVE_RADIUS * sectorHeight;

        world = new GameWorld(GameConfig.PLAYERS_NUM, sectorHeight, ballRadius);
        world.getBall().setVelocity(GameConfig.BALL_VELOCITY);  // TODO add randomization
    }

    private void setListeners() {
        // TODO set listeners
    }

    private void cleanMessageList() {
        final long time = System.currentTimeMillis();
        while (time - messageList.getFirst().getTimestamp() > GameConfig.STATE_KEEP_TIME) {
            messageList.pollFirst();
        }
    }

    private void resimulateGameWorld(
            GameWorld gameWorld,
            List<Message<GameWorldState>> stateMessageList,
            int lastMessageIndex
    ) {
        final List<Long> timeElapsedList = IntStream.range(lastMessageIndex + 1, stateMessageList.size()).boxed()
                .map(i -> stateMessageList.get(i).getTimestamp() - stateMessageList.get(i - 1).getTimestamp())
                .collect(Collectors.toList());

        for (int i = 0; i != timeElapsedList.size(); ++i) {
            gameWorld.makeIteration(timeElapsedList.get(i));
            gameWorld.setState(stateMessageList.get(lastMessageIndex + i + 1).getData());
        }
    }

    private void revertGameWorld(
            int playerId,
            PlatformState newPlatformState,
            GameWorldState lastState,
            GameWorld gameWorld
    ) {
        gameWorld.setState(lastState);
        gameWorld.getPlatforms().get(playerId)
                .setState(newPlatformState.getDiscreteRotation(playerId, GameConfig.PLAYERS_NUM));
    }

    @Nullable
    private Integer getSnapshotIndex(int stateId) {
        for (int i = 0; i != messageList.size(); ++i) {
            if (messageList.get(i).getId() == stateId) {
                return i;
            }
        }

        return null;
    }
}