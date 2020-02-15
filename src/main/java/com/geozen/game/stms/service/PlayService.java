package com.geozen.game.stms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.geozen.game.stms.domain.PlayStage;
import com.geozen.game.stms.domain.Player;
import com.geozen.game.stms.domain.Room;
import com.geozen.game.stms.enums.Card;
import com.geozen.game.stms.enums.CardTimes;
import com.geozen.game.stms.enums.PlayerStatus;
import com.geozen.game.stms.enums.RoomStatus;
import com.geozen.game.stms.exception.BusinessException;

@Service
public class PlayService {

	private Map<String, Room> roomMap = new ConcurrentHashMap<String, Room>();

	/**
	 * 创建房间
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public String create(String nickname) {
		while (true) {
			String roomNumber = RandomStringUtils.randomNumeric(6);
			if (roomMap.get(roomNumber) == null) {
				Room room = new Room();
				room.setHost(nickname);
				room.addPlayer(nickname);
				room.setStatus(RoomStatus.Wait);
				roomMap.put(roomNumber, room);
				return roomNumber;
			}
		}
	}

	/**
	 * 加入房间
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public void join(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room == null) {
			throw new BusinessException("没有此房间");
		}
//		if (room.isOccupied(nickname)) {
//			throw new BusinessException("用户已存在，请换个昵称");
//		}
		room.addPlayer(nickname);
//		switch (room.getStatus()) {
//		case Wait:
//			room.addPlayer(nickname);
//			break;
//		case End:
//			throw new BusinessException("游戏结束，不允许进入房间");
//		case In:
//			throw new BusinessException("正在游玩中，不允许进入房间");
//		}
	}

	/**
	 * 退出房间
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public void exit(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room == null) {
			throw new BusinessException("没有此房间");
		}
		if (room.getStatus().equals(RoomStatus.In)) {
			throw new BusinessException("正在游戏中，不能退出");
		} else if (room.getStatus().equals(RoomStatus.Wait)) {
			room.removePlayer(nickname);
		}
	}

	/**
	 * 开始发牌
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public void startGame(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room == null) {
			throw new BusinessException("房间无效，请重新开通房间再开始发牌");
		}
		if (!room.isHost(nickname)) {
			throw new BusinessException("不是房主，不能开始发牌");
		}
		if (room.getPlayers().size() < 2) {
			throw new BusinessException("必须2个人以上才能游戏");
		}
		room.getPlayers().stream().forEach(player -> player.reset());
		PlayStage stage = new PlayStage();
		stage.setPlayers(new ArrayList<>(room.getPlayers()));
		stage.assginCards(2); // 每人两张牌
		room.setStage(stage);
		room.setStatus(RoomStatus.In);
	}

	/**
	 * 获取房间信息
	 * 
	 * @param roomNumber
	 * @return
	 */
	public Room get(String roomNumber) {
		return roomMap.get(roomNumber);
	}

	/**
	 * 结束游戏
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public void endGame(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room != null) {
			if (!room.isHost(nickname)) {
				throw new BusinessException("不是房主，不能结束游戏");
			}
			if (room.getStatus().equals(RoomStatus.In) && !room.getPlayers().stream().allMatch(player -> PlayerStatus.Locked.equals(player.getStatus()))) {
				throw new BusinessException("本局未结束，不能结束游戏");
			}
			room.setStatus(RoomStatus.End);
		}
	}

	/**
	 * 关闭房间
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public void close(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room != null && room.isHost(nickname)) {
			roomMap.remove(roomNumber);
		}
	}

	/**
	 * 锁牌
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	@SuppressWarnings("incomplete-switch")
	public void lock(String roomNumber, String nickname, boolean toFill) {
		Room room = roomMap.get(roomNumber);
		if (room != null) {
			PlayStage stage = room.getStage();
			Player player = room.getPlayer(nickname);
			if (!PlayerStatus.Locked.equals(player.getStatus())) {
				if (toFill) {
					if (stage.getCurrentTurn() != player.getIndex()) {
						throw new BusinessException("还没轮到您");
					}
					stage.fillCard(player);
				}
				int ghostCount = 0;
				for (Card card : player.getCards()) {
					if (card.equals(room.getStage().getExtraGhost())) {
						ghostCount++;
					} else {
						switch (card) {
						case JK1:
						case JK2:
							ghostCount++;
							break;
						}
					}
				}
				if (ghostCount == 3) {
					player.setTimes(CardTimes.TrippleGhost);
				} else if (ghostCount == 2) {
					if (player.getCards().size() == 2) {
						player.setTimes(CardTimes.DoubleGhost);
					} else {
						player.setTimes(CardTimes.Flush);
					}
				} else {
					if (ghostCount == 1 && player.getCards().size() == 2) {
						throw new BusinessException("必须补牌");
					}
					if (ghostCount == 1) {
						Collections.sort(player.getCards(), new Comparator<Card>() {

							@Override
							public int compare(Card o1, Card o2) {
								Integer idx1 = o1.getIndex();
								Integer idx2 = o2.getIndex();
								return idx1.compareTo(idx2);
							}
						});
						int sameTypeCount = 0;
						int points = 0;
						String lastType = null;
						for (Card card : player.getCards()) {
							points += card.getPoint();
							if (lastType != null && lastType.equals(card.getType())) {
								sameTypeCount++;
							}
							lastType = card.getType();
						}
						points = points % 10;
						player.setPoints(points);
						boolean isSameType = sameTypeCount == 1;
						int delta = player.getCards().get(2).getIndex() - player.getCards().get(1).getIndex();
						if (delta > 0 && delta <= 2 || delta >= 11) {
							if (isSameType) {
								player.setTimes(CardTimes.Flush);
							} else {
								player.setTimes(CardTimes.Straight);
							}
						} else if (delta == 0) {
							player.setTimes(CardTimes.SamePoints);
						} else {
							player.setTimes(CardTimes.General);
							player.setPoints(9);
						}
					} else {
						if (player.getCards().size() == 2) {
							int sameTypeCount = 0;
							int points = 0;
							String lastType = null;
							for (Card card : player.getCards()) {
								points += card.getPoint();
								if (lastType != null && lastType.equals(card.getType())) {
									sameTypeCount++;
								}
								lastType = card.getType();
							}
							points = points % 10;
							player.setPoints(points);
							// 木虱
							if (points == 0) {
								player.setTimes(CardTimes.Bug);
							} else {
								boolean isGodPoints = points > 7;
								boolean isSameType = sameTypeCount == 1;
								if (isGodPoints) {
									// 双倍天公
									if (isSameType) {
										player.setTimes(CardTimes.DoubleGod);
									} else {
										player.setTimes(CardTimes.God);
									}
								} else {
									// 两条同花
									if (isSameType) {
										player.setTimes(CardTimes.DoubleSameType);
									} else {
										player.setTimes(CardTimes.General);
									}
								}
							}
						} else {
							int sameTypeCount = 0;
							int points = 0;
							String lastType = null;
							Collections.sort(player.getCards(), new Comparator<Card>() {

								@Override
								public int compare(Card o1, Card o2) {
									Integer i1 = o1.getIndex();
									Integer i2 = o2.getIndex();
									return i1.compareTo(i2);
								}
							});
							int continueIndexCount = 0;
							Card lastCard = null;
							for (Card card : player.getCards()) {
								points += card.getPoint();
								if (lastCard != null) {
									switch (lastCard.getIndex()) {
									case 13: {
										if (card.getIndex() == 1) {
											continueIndexCount++;
										}
									}
										break;
									default: {
										if (card.getIndex() - lastCard.getIndex() == 1) {
											continueIndexCount++;
										}
									}
									}
								}
								if (lastType != null && lastType.equals(card.getType())) {
									sameTypeCount++;
								}
								lastType = card.getType();
								lastCard = card;
							}
							boolean isSamePoint = points % 3 == 0;
							points = points % 10;
							player.setPoints(points);
							boolean isSameType = sameTypeCount == 2;
							boolean isStraight = continueIndexCount == 2;
							if (isSameType) {
								if (isStraight) {
									player.setTimes(CardTimes.Flush);
								} else {
									player.setTimes(CardTimes.TrippleSameType);
								}
							} else {
								if (isStraight) {
									player.setTimes(CardTimes.Straight);
								} else if (isSamePoint) {
									player.setTimes(CardTimes.TrippleSameType);
								} else if (points == 0) {
									player.setTimes(CardTimes.Bug);
								} else {
									player.setTimes(CardTimes.General);
								}
							}
						}
					}
				}
				player.setStatus(PlayerStatus.Locked);
				room.getStage().nextTurn();
			}
		}
	}

	/**
	 * 计算结果
	 * 
	 * @param roomNumber
	 * @param nickname
	 */
	public void calResult(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room != null) {
			if (!room.isHost(nickname)) {
				throw new BusinessException("不是房主，不能计算结果");
			}
			for (Player player : room.getPlayers()) {
				player.setStageAmount(0);
				for (Player comparePlayer : room.getPlayers()) {
					if (player.equals(comparePlayer)) {
						continue;
					}
					if(CardTimes.Bug.equals(player.getTimes()) && CardTimes.DoubleGhost.equals(comparePlayer.getTimes())) {
						player.setStageAmount(player.getStageAmount() + comparePlayer.getTimes().getValue());
					} else if (player.getTimes().getPriority() < comparePlayer.getTimes().getPriority()) {
						player.setStageAmount(player.getStageAmount() + player.getTimes().getValue());
					} else if (player.getTimes().getPriority() > comparePlayer.getTimes().getPriority()) {
						player.setStageAmount(player.getStageAmount() - comparePlayer.getTimes().getValue());
					} else if (player.getPoints() > comparePlayer.getPoints()) {
						player.setStageAmount(player.getStageAmount() + player.getTimes().getValue());
					} else if (player.getPoints() < comparePlayer.getPoints()) {
						player.setStageAmount(player.getStageAmount() - comparePlayer.getTimes().getValue());
					}
				}
				player.setAmount(player.getAmount() + player.getStageAmount());
			}
		}
	}

}
