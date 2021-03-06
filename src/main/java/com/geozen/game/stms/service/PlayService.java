package com.geozen.game.stms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
				room.setNumber(roomNumber);
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
	public boolean join(String roomNumber, String nickname) {
		Room room = roomMap.get(roomNumber);
		if (room == null) {
			throw new BusinessException("没有此房间");
		}

//		if (player != null && !PlayerStatus.Exit.equals(player.getStatus())) {
//			throw new BusinessException("用户已存在，请换个昵称");
//		}
		switch (room.getStatus()) {
		case Calculated:
		case Wait: {
			room.addPlayer(nickname);
		}
			break;
		case In: {
			Player player = room.getPlayer(nickname);
			if (player == null) {
				throw new BusinessException("游戏进行中，不能加入");
			}
		}
			break;
		case End:
			throw new BusinessException("游戏已结束");
		}
		return room.getHost().equals(nickname);
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
		if (RoomStatus.Wait.equals(room.getStatus())) {
			room.removePlayer(nickname);
		} else {
			Player player = room.getPlayer(nickname);
			player.setStatus(PlayerStatus.Exit);
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
		int count = 0;
		for (Player player : room.getPlayers()) {
			if (!PlayerStatus.Exit.equals(player.getStatus())) {
				count++;
			}
		}
		if (count < 2) {
			throw new BusinessException("必须2个人以上才能游戏");
		}
		room.getPlayers().stream().forEach(player -> {
			if (!PlayerStatus.Exit.equals(player.getStatus()))
				player.reset();
		});
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
					if (card.getIndex() == room.getStage().getExtraGhost().getIndex()) {
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
						List<Card> sortedCards = new ArrayList<>(player.getCards());
						Collections.sort(sortedCards, new Comparator<Card>() {
							@Override
							public int compare(Card o1, Card o2) {
								Integer idx1 = o1.getIndex();
								Integer idx2 = o2.getIndex();
								if (room.getStage().getExtraGhost().getIndex() == idx1) {
									return -1;
								} else if (room.getStage().getExtraGhost().getIndex() == idx2) {
									return 1;
								}
								return idx1.compareTo(idx2);
							}
						});
						int sameTypeCount = 0;
						int points = 0;
						String lastType = null;
						for (Card card : sortedCards) {
							points += card.getPoint();
							if (lastType == null || lastType.equals(card.getType())) {
								sameTypeCount++;
							}
							if (card.getIndex() == 0 || room.getStage().getExtraGhost().getIndex() == card.getIndex()) {
								lastType = null;
							} else {
								lastType = card.getType();
							}
						}
						points = points % 10;
						player.setPoints(points);
						boolean isSameType = sameTypeCount == 3;
						int delta = sortedCards.get(2).getIndex() - sortedCards.get(1).getIndex();
						if (delta > 0 && delta <= 2 || delta >= 11) {
							if (isSameType) {
								player.setTimes(CardTimes.Flush);
							} else {
								player.setTimes(CardTimes.Straight);
							}
						} else if (delta == 0) {
							player.setTimes(CardTimes.SamePoints);
							player.setPoints(sortedCards.get(1).getIndex());
						} else {
							if (isSameType) {
								player.setTimes(CardTimes.TrippleSameType);
							} else {
								player.setTimes(CardTimes.General);
							}
							player.setPoints(9);
						}
					} else {
						if (player.getCards().size() == 2) {
							int sameTypeCount = 1;
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
								boolean isSameType = sameTypeCount == 2;
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
							List<Card> sortedCards = new ArrayList<>(player.getCards());
							Collections.sort(sortedCards, new Comparator<Card>() {
								@Override
								public int compare(Card o1, Card o2) {
									Integer i1 = o1.getIndex();
									Integer i2 = o2.getIndex();
									return i1.compareTo(i2);
								}
							});
							int sameTypeCount = 1;
							int points = 0;
							String lastType = null;
							boolean isStraight = false;
							int samePointCount = 1;
							Card firstCard = sortedCards.get(0);
							Card middleCard = sortedCards.get(1);
							Card lastCard = sortedCards.get(2);
							if (middleCard.getIndex() - firstCard.getIndex() == 1 || middleCard.getIndex() - firstCard.getIndex() == 11) {
								if (lastCard.getIndex() - middleCard.getIndex() == 1 || lastCard.getIndex() - middleCard.getIndex() == 11) {
									isStraight = true;
								}
							}
							for (Card card : sortedCards) {
								points += card.getPoint();
								if (lastType != null && lastType.equals(card.getType())) {
									sameTypeCount++;
								}
								lastType = card.getType();
								lastCard = card;
							}
							boolean isSamePoint = samePointCount == 3;
							points = points % 10;
							player.setPoints(points);
							boolean isSameType = sameTypeCount == 3;
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
									player.setTimes(CardTimes.SamePoints);
									player.setPoints(sortedCards.get(0).getIndex());
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

//	public static void main(String[] args) {
//		PlayService p = new PlayService();
//		String nickname = "test";
//		String roomNumber = p.create(nickname);
//		Room room = p.get(roomNumber);
//		Player py = room.getPlayer(nickname);
//		PlayStage stage = new PlayStage();
//		stage.setPlayers(java.util.Arrays.asList(py));
//		room.setStage(stage);
//		py.addCards(Card.C9);
//		py.addCards(Card.H7);
//		py.addCards(Card.C7);
//		p.lock(roomNumber, nickname, false);
//		System.out.println(py.getTimes().getName() + ", " + py.getPoints());
//	}

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
				if (PlayerStatus.Exit.equals(player.getStatus())) {
					continue;
				}
				player.setStageAmount(0);
				for (Player comparePlayer : room.getPlayers()) {
					if (player.equals(comparePlayer)) {
						continue;
					}
					if (CardTimes.Bug.equals(player.getTimes()) && CardTimes.DoubleGhost.equals(comparePlayer.getTimes())) {
						player.setStageAmount(player.getStageAmount() + comparePlayer.getTimes().getValue());
					} else if (player.getTimes().getPriority() < comparePlayer.getTimes().getPriority()) {
						player.setStageAmount(player.getStageAmount() + player.getTimes().getValue());
					} else if (player.getTimes().getPriority() > comparePlayer.getTimes().getPriority()) {
						player.setStageAmount(player.getStageAmount() - comparePlayer.getTimes().getValue());
					} else if (player.getPoints() > comparePlayer.getPoints()) {
						player.setStageAmount(player.getStageAmount() + player.getTimes().getValue());
					} else if (player.getPoints() < comparePlayer.getPoints()) {
						player.setStageAmount(player.getStageAmount() - comparePlayer.getTimes().getValue());
					} else if (player.getTimes().getValue() > comparePlayer.getTimes().getValue()) {
						player.setStageAmount(player.getStageAmount() + player.getTimes().getValue());
					} else if (player.getTimes().getValue() < comparePlayer.getTimes().getValue()) {
						player.setStageAmount(player.getStageAmount() - comparePlayer.getTimes().getValue());
					}
				}
				player.setAmount(player.getAmount() + player.getStageAmount());
			}
			room.setStatus(RoomStatus.Calculated);
		}
	}

}
